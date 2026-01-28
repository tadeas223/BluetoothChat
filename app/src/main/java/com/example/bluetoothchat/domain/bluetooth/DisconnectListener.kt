package com.example.bluetoothchat.domain.bluetooth

interface DisconnectListener {
    fun onDisconnected(connection: Connection)
}