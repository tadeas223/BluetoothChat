package com.example.bluetoothchat.domain.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface BluetoothScanService {
    val scannedDevices: StateFlow<List<Device>>
    val requiredPermissions: List<String>

    fun startScan()
    fun stopScan()
}