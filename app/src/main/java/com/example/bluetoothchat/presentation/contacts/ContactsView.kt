package com.example.bluetoothchat.presentation.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluetoothchat.presentation.components.ClickableCard
import com.example.bluetoothchat.presentation.components.ClickableCardItem

@Composable
fun ContactsView(onAddUser: () -> Unit) {
    val viewModel = hiltViewModel<ContactsViewModel>()
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())

    Column {
        Row (
            modifier = Modifier.padding(10.dp)
        ){
            Button(onClick = onAddUser) {
                Text("add user")
            }
        }
        Row {
            ClickableCard(
                contacts.map {
                    ClickableCardItem(it.username, {println("clicked")})
                }
            )
        }
    }
}