package com.example.bluetoothchat.presentation.add_user

import android.graphics.DiscretePathEffect
import android.view.inputmethod.CorrectionInfo
import androidx.lifecycle.ViewModel
import com.example.bluetoothchat.data.bluetooth.BluetoothScanner
import com.example.bluetoothchat.data.user.contact.ContactRepository
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.domain.user.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddUserViewModel @Inject constructor(
    private val bluetoothScanner: BluetoothScanner,
    private val contactRepository: ContactRepository,
): ViewModel() {
    val scannedDevices = bluetoothScanner.scannedDevices

    fun startScan() {
        bluetoothScanner.startScan()
    }

    fun stopScan() {
        bluetoothScanner.stopScan()
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