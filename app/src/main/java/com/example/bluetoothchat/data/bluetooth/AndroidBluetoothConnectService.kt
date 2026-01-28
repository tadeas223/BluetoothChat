package com.example.bluetoothchat.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.content.Context
import android.util.Log
import com.example.bluetoothchat.data.hasPermissions
import com.example.bluetoothchat.domain.bluetooth.BluetoothConnectService
import com.example.bluetoothchat.domain.bluetooth.Connection
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class AndroidBluetoothConnectService @Inject constructor(
    @ApplicationContext val context: Context,
    val chatMessageRepository: ChatMessageRepository,
    val contactRepository: ContactRepository
): BluetoothConnectService {
    companion object {
        const val SERVICE_UUID = "2bf1c8cf-f803-440d-8c91-b51f5115f232"
    }

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var serverJob: Job? = null

    private val _activeConnections = MutableStateFlow<Map<Device, Connection>>(emptyMap())
    override val requiredPermissions = listOf(
        Manifest.permission.BLUETOOTH_CONNECT,
    )

    override val activeConnections
        get() = _activeConnections.asStateFlow()

    private var currentServerSocket: BluetoothServerSocket? = null

    @SuppressLint("MissingPermission")
    override fun startServer() {
        if(!hasPermissions(context, requiredPermissions))  {
            throw SecurityException("missing required permissions")
        }

        serverJob = CoroutineScope(Dispatchers.IO).launch {
            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service",
                UUID.fromString(SERVICE_UUID)
            )

            while (isActive) {
                val clientSocket = try {
                    currentServerSocket?.accept()
                } catch (_: IOException) {
                    break
                }

                if(clientSocket != null) {
                    val device = clientSocket.remoteDevice.toDevice()
                    val connection = ClientBluetoothConnection( clientSocket, chatMessageRepository)

                    connection.addDisconnectListener(ClientConnectionDisconnectListener {
                        val current = _activeConnections.value.toMutableMap()
                        var device: Device? = null
                        current.forEach { entry ->
                            if(entry.value == connection) {
                                device = entry.key
                                return@forEach
                            }
                        }

                        if(device != null) {
                            current.remove(device)
                        }

                        _activeConnections.value = current.toMap()
                    })

                    connection.addReceiveListener(AndroidBluetoothListener(connection, contactRepository, chatMessageRepository))

                    val current = _activeConnections.value.toMutableMap()
                    current[device] = connection
                    _activeConnections.value = current.toMap()
                }

            }
        }
    }

    override fun stopServer() {
        if(!hasPermissions(context, requiredPermissions))  {
            throw SecurityException("missing required permissions")
        }

        serverJob?.cancel()
        currentServerSocket?.close()
    }

    override suspend fun connect(address: String): Connection? {
        if(!hasPermissions(context, requiredPermissions))  {
            throw SecurityException("missing required permissions")
        }

        val connection = ClientBluetoothConnection(chatMessageRepository)

        val blDevice = bluetoothAdapter?.getRemoteDevice(address)
        val socket = blDevice?.createRfcommSocketToServiceRecord(UUID.fromString(SERVICE_UUID))
        if(socket != null) {
            connection.connectToSocket(socket)
        } else {
            return null
        }

        return connection
    }

}