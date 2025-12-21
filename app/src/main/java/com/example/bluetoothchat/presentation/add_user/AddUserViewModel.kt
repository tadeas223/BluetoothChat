package com.example.bluetoothchat.presentation.add_user

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.bluetoothchat.domain.bluetooth.BluetoothConnectService
import com.example.bluetoothchat.domain.bluetooth.BluetoothScanService
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.domain.user.contact.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val bluetoothConnectService: BluetoothConnectService,
    private val bluetoothScanService: BluetoothScanService,
    private val contactRepository: ContactRepository,
): ViewModel() {
    val scannedDevices = bluetoothScanService.scannedDevices

    fun startScan() {
        bluetoothScanService.startScan()
    }

    fun stopScan() {
        bluetoothScanService.stopScan()
    }

    fun addContact(device: Device, name: String) {
        CoroutineScope(Dispatchers.Default).launch {
            val connection = bluetoothConnectService.connect(device.address) ?: return@launch
            if(connection.address == null) return@launch

            contactRepository.insert(Contact(0, name, connection.address!!))
            Log.d("BluetoothChat", "adding contact, advertise: ${device.address}, real: ${connection.address}")
            connection.disconnect()
        }
    }

    override fun onCleared() {
        super.onCleared()

        stopScan()
    }
}