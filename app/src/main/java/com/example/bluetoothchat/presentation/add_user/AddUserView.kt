package com.example.bluetoothchat.presentation.add_user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.domain.user.Contact
import com.example.bluetoothchat.presentation.add_user.components.AddScreen
import com.example.bluetoothchat.presentation.add_user.components.ScanScreen
import com.example.bluetoothchat.presentation.components.ClickableCard
import com.example.bluetoothchat.presentation.components.ClickableCardItem

@Composable
fun AddUserView(onBack: () -> Unit) {
    val viewModel = hiltViewModel<AddUserViewModel>()
    val scannedDevices by viewModel.scannedDevices.collectAsState()
    viewModel.startScan()

    var selected: Device? = null;

    val navController = rememberNavController()
    NavHost(navController, startDestination = "scan") {
        composable("scan") {
            ScanScreen(
                scannedDevices,
                onDeviceSelect = {
                    selected = it
                    navController.navigate("add")
                },
                onBack = {
                    onBack()
                }
            )
        }

        composable("add") {
            if(selected == null) {
                navController.navigate("scan")
            }

            AddScreen(
                device = selected!!,
                onAdd = {
                    viewModel.addContact(selected, it)
                    onBack()
                }
            )
        }
    }

}