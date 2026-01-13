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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val messages: StateFlow<List<ChatMessage>> = _contactId
        .flatMapLatest { id ->
            if (id != null) messageRepository.selectByContactId(id)
            else emptyFlow()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    private var _isConnected = MutableStateFlow(false)
    val isConnected: StateFlow<Boolean>
        get() = _isConnected.asStateFlow()

    fun setContact(id: Int) {
        viewModelScope.launch {
            _contactId.value = id

            _contact.value = withContext(Dispatchers.IO) {
                contactRepository.selectById(id).firstOrNull()
            }

            _contact.value?.let {
                try {
                    connection = withContext(Dispatchers.IO) {
                        bluetoothConnectService.connect(it.address)
                    }

                    connection?.let { conn ->
                        viewModelScope.launch {
                            conn.isConnected.collect { connected ->
                                _isConnected.value = connected
                            }
                        }
                    }
                } catch(e: Exception) {
                }
            }
        }
    }
    fun sendMessage(text: String) {
        viewModelScope.launch {
            val connected = _isConnected.value

            if(connected && contact.value != null) {
                val message = ChatMessage(text = text, contact = contact.value!!)
                connection?.send(message)
                messageRepository.insert(message)
            }
        }
    }

    suspend fun removeContact() {
        if(contact.value == null) {
            Log.w("BluetoothChat", "cannot delete null contact");
            return;
        }

        contactRepository.delete(contact.value!!);
    }

}
