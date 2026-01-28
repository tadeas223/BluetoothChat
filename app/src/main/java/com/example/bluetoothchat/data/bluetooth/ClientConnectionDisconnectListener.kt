package com.example.bluetoothchat.data.bluetooth

import com.example.bluetoothchat.domain.bluetooth.BluetoothConnectService
import com.example.bluetoothchat.domain.bluetooth.Connection
import com.example.bluetoothchat.domain.bluetooth.DisconnectListener
import javax.inject.Inject

class ClientConnectionDisconnectListener (
    val onDisconnectedCallback: (connection: Connection) -> Unit
) : DisconnectListener {
    override fun onDisconnected(connection: Connection) {
        onDisconnectedCallback(connection)
    }
}