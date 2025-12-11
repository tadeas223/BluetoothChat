package com.example.bluetoothchat.data.bluetooth

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

class AndroidBluetoothConnector @Inject constructor(
    @ApplicationContext val context: Context
): BluetoothConnector {
    companion object {
        const val SERVICE_UUID = "2bf1c8cf-f803-440d-8c91-b51f5115f232"
    }

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private var currentServerSocket: BluetoothServerSocket? = null
    private var currentClientSocket: BluetoothSocket? = null

    override fun startServer(): Flow<ConnectionResult> {
        return flow {
            if(!hasPermissions(context, Manifest.permission.BLUETOOTH_CONNECT)) {
                throw SecurityException("no bluetooth connect permission")
            }

            currentServerSocket = bluetoothAdapter?.listenUsingRfcommWithServiceRecord(
                "chat_service",
                UUID.fromString(SERVICE_UUID)
            )

            var shouldLoop = true
            while(shouldLoop) {
                currentClientSocket = try {
                    currentServerSocket?.accept()
                } catch(e: IOException) {
                    shouldLoop = false
                    null
                }
            }
        }
    }

    override fun connectToAddress(address: String): Flow<ConnectionResult> {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

}