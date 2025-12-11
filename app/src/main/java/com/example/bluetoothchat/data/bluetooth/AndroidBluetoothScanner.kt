package com.example.bluetoothchat.data.bluetooth

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.RequiresPermission
import com.example.bluetoothchat.domain.bluetooth.Device
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AndroidBluetoothScanner @Inject constructor(
    @ApplicationContext private val context: Context
): BluetoothScanner {

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

    override fun startScan() {
        if(isScanning) {
            return
        } else {
            isScanning = true
        }

        if(!hasPermissions(context,Manifest.permission.BLUETOOTH_SCAN)) {
            Log.w("BluetoothChat", "startScan() -> does not have permission BLUETOOTH_SCAN")
            return
        }

        context.registerReceiver(
            foundDeviceReceiver,
            IntentFilter(BluetoothDevice.ACTION_FOUND)
        )

        Log.d("BluetoothChat", "BluetoothAdapter enabled: ${bluetoothAdapter?.isEnabled}")
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopScan() {
        if(!isScanning) {
            return
        } else {
            isScanning = false
        }

        if(!hasPermissions(context,Manifest.permission.BLUETOOTH_SCAN)) {
            Log.w("BluetoothChat", "stopScan() -> does not have permission BLUETOOTH_SCAN")
            return
        }

        _scannedDevices.value = emptyList()

        bluetoothAdapter?.cancelDiscovery()
        context.unregisterReceiver(foundDeviceReceiver)
    }

}