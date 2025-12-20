package com.example.bluetoothchat.presentation.add_user

import androidx.lifecycle.ViewModel
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
            contactRepository.insert(Contact(0, name, device.address))
        }
    }

    override fun onCleared() {
        super.onCleared()

        stopScan()
    }
}