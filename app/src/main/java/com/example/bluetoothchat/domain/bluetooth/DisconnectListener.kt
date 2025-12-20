package com.example.bluetoothchat.domain.bluetooth

interface DisconnectListener {
    fun disconnected(connection: Connection)
}