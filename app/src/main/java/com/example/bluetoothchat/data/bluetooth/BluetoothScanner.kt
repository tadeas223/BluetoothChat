package com.example.bluetoothchat.data.bluetooth

import com.example.bluetoothchat.data.model.Device
import kotlinx.coroutines.flow.StateFlow

interface BluetoothScanner {
    val scannedDevices: StateFlow<List<Device>>

    fun startScan()
    fun stopScan()

    fun destroy()
}