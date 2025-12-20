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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val contactRepository: ContactRepository,
    val messageRepository: ChatMessageRepository,
    val bluetoothConnectService: BluetoothConnectService
) : ViewModel() {
    private var connection: Connection? = null

    private val _contactId = MutableStateFlow<Int?>(null)
    private val _contact = MutableStateFlow<Contact?>(null)

    val contact: StateFlow<Contact?> = _contact

    val messages: StateFlow<List<ChatMessage>> = _contactId
        .flatMapLatest { id ->
            if (id != null) messageRepository.selectByContactId(id)
            else emptyFlow()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    var isConnected: Flow<Boolean> = emptyFlow()

    fun setContact(id: Int) {
        viewModelScope.launch {
            _contactId.value = id

            _contact.value = withContext(Dispatchers.IO) {
                contactRepository.selectById(id).firstOrNull()
            }

            _contact.value?.let {
                connection = withContext(Dispatchers.IO) {
                    bluetoothConnectService.connect(it.address)
                }
                isConnected = connection?.isConnected ?: emptyFlow()
            }
        }
    }
    fun sendMessage(text: String) {
        var connected = false
        viewModelScope.launch {
            isConnected.collect {
                connected = it
            }

            if(connected && contact.value != null) {
                messageRepository.insert(ChatMessage(text = text, contact = contact.value!!))
            }
        }
    }

}
