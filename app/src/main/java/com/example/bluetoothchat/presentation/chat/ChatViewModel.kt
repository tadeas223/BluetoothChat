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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.isActive

@HiltViewModel
class ChatViewModel @Inject constructor(
    val contactRepository: ContactRepository,
    val messageRepository: ChatMessageRepository,
    val bluetoothConnectService: BluetoothConnectService
) : ViewModel() {
    private var connection: Connection? = null

    private val _contactId = MutableStateFlow<Int?>(null)
    private val _contact = MutableStateFlow<Contact?>(null)
    private var reconnectJob: Job? = null


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
        reconnectJob?.cancel()

        reconnectJob = viewModelScope.launch {
            _contactId.value = id

            val contact = withContext(Dispatchers.IO) {
                contactRepository.selectById(id).firstOrNull()
            } ?: return@launch

            _contact.value = contact

            autoReconnect(contact.address)
        }
    }

    private suspend fun autoReconnect(address: String) {
        while (currentCoroutineContext().isActive) {
            Log.d("BluetoothChat", "Attempting to connect...")

            val conn = withContext(Dispatchers.IO) {
                bluetoothConnectService.connect(address)
            }

            if (conn == null) {
                delay(2_000)
                continue
            }

            connection = conn
            observeConnection(conn)

            // Wait until disconnected
            conn.isConnected
                .filter { !it }
                .first()

            Log.d("BluetoothChat", "Disconnected, retrying...")
            delay(2_000)
        }
    }

    private fun observeConnection(conn: Connection) {
        conn.isConnected
            .onEach { connected ->
                _isConnected.value = connected
                Log.d("BluetoothChat", "Connected = $connected")
            }
            .launchIn(viewModelScope)
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            val connected = _isConnected.value

            if(!connected) {
                return@launch;
            }

            if(contact.value != null) {
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
