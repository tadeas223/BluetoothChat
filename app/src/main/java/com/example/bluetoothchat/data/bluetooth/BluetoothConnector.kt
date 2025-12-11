package com.example.bluetoothchat.data.bluetooth

import com.example.bluetoothchat.domain.bluetooth.Device
import kotlinx.coroutines.flow.Flow

interface BluetoothConnector {
    fun startServer(): Flow<ConnectionResult>
    fun connectToAddress(address: String): Flow<ConnectionResult>

    fun close()
}