package com.example.bluetoothchat.presentation.contacts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluetoothchat.presentation.components.ClickableCard
import com.example.bluetoothchat.presentation.components.ClickableCardItem
import kotlinx.coroutines.flow.map

@Composable
fun ContactsView(
    onAddUser: () -> Unit,
    onContactSelect: (id: Int) -> Unit
) {
    val viewModel = hiltViewModel<ContactsViewModel>()
    val contacts by viewModel.contacts.collectAsState(initial = emptyList())

    Column {
        Row (
            modifier = Modifier.padding(10.dp)
        ){
            Button(onClick = onAddUser) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
            }
        }
        Row {
            ClickableCard(
                contacts.map { contact ->
                    ClickableCardItem(contact.username, {onContactSelect(contact.id)})
                }
            )
        }
    }
}