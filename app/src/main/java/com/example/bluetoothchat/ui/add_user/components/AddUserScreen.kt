package com.example.bluetoothchat.ui.add_user.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bluetoothchat.data.model.Device

@Composable
fun AddUserScreen(
    scannedDevices: List<Device>,
    onBackBtn: () -> Unit
) {
    Box {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            Row (){
                LazyColumn {
                    items(scannedDevices) { device ->
                        device.name?.let { name ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable {
                                        println("Clicked on $name")
                                    },
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = name, style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }                }
            }
        }
        Row (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly

        ){
            Button(onClick = onBackBtn) {
                Text("back")
            }
        }

    }
}