package com.example.bluetoothchat.data.bluetooth

import android.bluetooth.BluetoothDevice
import com.example.bluetoothchat.domain.bluetooth.Device

fun BluetoothDevice.toDevice(): Device {
    return Device(
        name = name,
        address = address
    )
}