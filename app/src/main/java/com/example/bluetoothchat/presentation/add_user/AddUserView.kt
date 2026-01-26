package com.example.bluetoothchat.presentation.add_user

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.presentation.add_user.components.AddScreen
import com.example.bluetoothchat.presentation.add_user.components.ScanScreen

@Composable
fun AddUserView(onBack: () -> Unit) {
    val viewModel = hiltViewModel<AddUserViewModel>()
    val scannedDevices by viewModel.scannedDevices.collectAsState()

    var selected: Device? = null
    val navController = rememberNavController()
    NavHost(navController, startDestination = "scan") {
        composable("scan") {
            ScanScreen(
                viewModel,
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