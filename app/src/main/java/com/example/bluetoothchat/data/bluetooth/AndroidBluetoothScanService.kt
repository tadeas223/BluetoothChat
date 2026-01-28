package com.example.bluetoothchat.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.example.bluetoothchat.data.hasPermissions
import com.example.bluetoothchat.domain.bluetooth.BluetoothScanService
import com.example.bluetoothchat.domain.bluetooth.Device
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.collections.plus

class AndroidBluetoothScanService @Inject constructor(
    @ApplicationContext private val context: Context
): BluetoothScanService {
    override val requiredPermissions = listOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_ADVERTISE
        )

    private var isScanning = false

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }

    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter
    }

    private val foundDeviceReceiver = FoundDeviceReceiver { device ->
        _scannedDevices.update { devices ->
            val newDevice = device.toDevice()
            if (newDevice in devices) {
                Log.d("BluetoothChat", "again scanned device: [${newDevice.address}] ${newDevice.name}")
                devices
            } else {
                Log.d("BluetoothChat", "scanned device: [${newDevice.address}] ${newDevice.name}")
                devices + newDevice
            }
        }
    }

    private val _scannedDevices = MutableStateFlow<List<Device>>(emptyList())
    override val scannedDevices: StateFlow<List<Device>>
        get() = _scannedDevices.asStateFlow()

    @SuppressLint("MissingPermission")
    override fun startScan() {
        if(!hasPermissions(context, requiredPermissions)) {
            throw SecurityException("missing required permissions")
        }

        if(isScanning) {
            return
        } else {
            isScanning = true
        }

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        Log.d("BluetoothChat", "BluetoothAdapter enabled: ${bluetoothAdapter?.isEnabled}")
        bluetoothAdapter?.startDiscovery()
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        if(!hasPermissions(context, requiredPermissions)) {
            throw SecurityException("missing required permissions")
        }

        if(!isScanning) {
            return
        } else {
            isScanning = false
        }

        _scannedDevices.value = emptyList()

        bluetoothAdapter?.cancelDiscovery()
        context.unregisterReceiver(foundDeviceReceiver)
    }
}