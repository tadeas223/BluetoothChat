package com.example.bluetoothchat.presentation.add_user.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.presentation.components.ClickableCard
import com.example.bluetoothchat.presentation.components.ClickableCardItem

@Composable
fun ScanScreen(
    scannedDevices: List<Device>,
    onDeviceSelect: (Device) -> Unit,
    onBack: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            Row () {
                ClickableCard(scannedDevices.map {
                    ClickableCardItem(it.name ?: "(no name)", {
                        onDeviceSelect(it)
                    })
                })
            }
        }
        Row (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ){
            Button(onClick = onBack) {
                Text("back")
            }
        }
    }

}