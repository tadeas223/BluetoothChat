package com.example.bluetoothchat.data.bluetooth

import android.bluetooth.BluetoothSocket
import androidx.compose.runtime.MutableState
import com.example.bluetoothchat.domain.bluetooth.Connection
import com.example.bluetoothchat.domain.bluetooth.ReceiveListener
import com.example.bluetoothchat.domain.bluetooth.DisconnectListener
import com.example.bluetoothchat.domain.bluetooth.Transferable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.compose
import kotlinx.coroutines.launch
import java.io.IOException

class ClientBluetoothConnection: Connection {
    private var socket: BluetoothSocket? = null

    private val _isConnected =  MutableStateFlow(false)
    override val isConnected: Flow<Boolean>
        get() = _isConnected


    private val receiveListeners = mutableSetOf<ReceiveListener>()
    private val disconnectListeners = mutableSetOf<DisconnectListener>()

    constructor() {}

    constructor(socket: BluetoothSocket) {
        this.socket = socket
        _isConnected.value = socket.isConnected
    }

    suspend fun connectToSocket(socket: BluetoothSocket) {
        try {
            socket.connect()
        } catch (_: IOException) {
            _isConnected.value = false
            callDisconnectListeners()
            return
        }
        this.socket = socket
        _isConnected.value = socket.isConnected
    }

    override fun addReceiveListener(listener: ReceiveListener) {
        receiveListeners.add(listener)
    }

    override fun removeReceiveListener(listener: ReceiveListener) {
        receiveListeners.remove(listener)
    }

    override fun addDisconnectListener(listener: DisconnectListener) {
        disconnectListeners.add(listener)
    }

    override fun removeDisconnectListener(listener: DisconnectListener) {
        disconnectListeners.remove(listener)
    }

    override suspend fun disconnect() {
        socket?.close()
        socket = null
        _isConnected.value = false
        callDisconnectListeners()
    }

    override suspend fun <T : Any> send(transferable: Transferable<T>) {
        val serialized = transferable.serialize()
        try {
            socket?.outputStream?.write(serialized)
        } catch(_: IOException) {
            disconnect()
        }
    }

    private fun callDisconnectListeners() {
        for (listener in disconnectListeners) {
            listener.onDisconnected()
        }
    }

}