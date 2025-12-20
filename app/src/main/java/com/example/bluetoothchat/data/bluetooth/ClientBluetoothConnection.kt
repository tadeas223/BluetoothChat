package com.example.bluetoothchat.data.bluetooth

import android.bluetooth.BluetoothSocket
import com.example.bluetoothchat.domain.bluetooth.BluetoothConnectService
import com.example.bluetoothchat.domain.bluetooth.Connection
import com.example.bluetoothchat.domain.bluetooth.ConnectionListener
import com.example.bluetoothchat.domain.bluetooth.DisconnectListener
import com.example.bluetoothchat.domain.bluetooth.Transferable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ClientBluetoothConnection: Connection {
    constructor() {}

    constructor(socket: BluetoothSocket) {
        this.socket = socket
    }

    private var socket: BluetoothSocket? = null

    suspend fun connectToSocket(socket: BluetoothSocket) {
        socket.connect()
        this.socket = socket
    }

    override val isConnected: Boolean
        get() {
            if(socket == null) {
                return false
            }
            return socket!!.isConnected
        }

    override fun addListener(listener: ConnectionListener) {
        TODO("Not yet implemented")
    }

    override fun removeListener(listener: ConnectionListener) {
        TODO("Not yet implemented")
    }

    override fun addDisconnectListener(listener: DisconnectListener) {
        TODO("Not yet implemented")
    }

    override fun removeDisconnectListener(listener: DisconnectListener) {
        TODO("Not yet implemented")
    }

    override suspend fun disconnect() {
        TODO("Not yet implemented")
    }

    override suspend fun send(transferable: Transferable<*>) {
        TODO("Not yet implemented")
    }

    override fun dispose() {
        TODO("Not yet implemented")
    }

}