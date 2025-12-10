package com.example.bluetoothchat.ui.add_user

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluetoothchat.data.model.Device
import com.example.bluetoothchat.ui.add_user.components.AddUserScreen
import kotlinx.coroutines.flow.StateFlow
@Composable
fun AddUserView() {
    val viewModel = hiltViewModel<AddUserViewModel>()
    val scannedDevices by viewModel.scannedDevices.collectAsState()
    viewModel.startScan()

    Surface() {
        AddUserScreen(scannedDevices) {}
    }
}