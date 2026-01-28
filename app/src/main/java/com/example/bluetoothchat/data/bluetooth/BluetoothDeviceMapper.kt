package com.example.bluetoothchat.data.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import com.example.bluetoothchat.domain.bluetooth.Device

@SuppressLint("MissingPermission")
fun BluetoothDevice.toDevice(): Device {
    return Device(
        name = name,
        address = address
    )
}