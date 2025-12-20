package com.example.bluetoothchat.domain.bluetooth

import kotlinx.coroutines.flow.Flow

interface BluetoothConnectService {
    val requiredPermissions: List<String>

    val activeConnections: Flow<Map<Device, Connection>>

    fun startServer()
    fun stopServer()

    suspend fun connect(device: Device): Connection?
    suspend fun disconnect(device: Device)
}