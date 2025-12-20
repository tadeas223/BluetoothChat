package com.example.bluetoothchat.presentation.chat

import android.util.Log
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bluetoothchat.domain.bluetooth.BluetoothConnectService
import com.example.bluetoothchat.domain.bluetooth.Connection
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import com.example.bluetoothchat.domain.bluetooth.Device
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import com.example.bluetoothchat.domain.user.contact.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val contactRepository: ContactRepository,
    val messageRepository: ChatMessageRepository,
    val bluetoothConnectService: BluetoothConnectService
) : ViewModel() {
    private var connection: Connection? = null
    private val _contact = MutableStateFlow<Contact?>(null)
    val contact: StateFlow<Contact?>
        get() = _contact.asStateFlow()

    private val _messages = MutableStateFlow(emptyList<ChatMessage>())

    val messages: StateFlow<List<ChatMessage>>
        get() = _messages.asStateFlow()

    fun setContact(id: Int) {
        viewModelScope.launch {
            _contact.value = contactRepository.selectById(id)
            if(contact.value != null) {
                connection = bluetoothConnectService.connect(contact.value!!.address)
            }

            messages = messageRepository.selectByContactFlow(contact)
        }
    }

}
