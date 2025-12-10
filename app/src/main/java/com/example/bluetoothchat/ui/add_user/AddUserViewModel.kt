package com.example.bluetoothchat.ui.add_user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bluetoothchat.data.bluetooth.BluetoothScanner
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val bluetoothScanner: BluetoothScanner
): ViewModel() {
    val scannedDevices = bluetoothScanner.scannedDevices

    fun startScan() {
        bluetoothScanner.startScan()
    }

    fun stopScan() {
        bluetoothScanner.stopScan()
    }
}