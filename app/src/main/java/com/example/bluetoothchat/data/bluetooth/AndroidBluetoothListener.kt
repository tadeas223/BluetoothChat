package com.example.bluetoothchat.data.bluetooth

import android.util.Log
import com.example.bluetoothchat.domain.bluetooth.AdvertisePacket
import com.example.bluetoothchat.domain.bluetooth.MessagePacket
import com.example.bluetoothchat.domain.bluetooth.ReceiveListener
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AndroidBluetoothListener(
    val connection: ClientBluetoothConnection,
    val contactRepository: ContactRepository,
    val chatMessageRepository: ChatMessageRepository
): ReceiveListener {
    private val receiveScope = CoroutineScope(Dispatchers.IO)

    override fun onMessagePacket(packet: MessagePacket) {
        receiveScope.launch {
            if(connection.address == null) {
                return@launch
            }

            Log.d("BluetoothChat", "message receiver called")
            val correctContact = contactRepository
                .selectByAddress(connection.address!!)

            val contact = async {
                correctContact.first()
            }.await();

            if(contact == null) {
                return@launch;
            }

            val newMessage = ChatMessage(
                0,
                packet.message,
                contact
            )
            chatMessageRepository.insert(newMessage)
        }
    }

    override fun onAdvertisePacket(packet: AdvertisePacket) {
        TODO("Not yet implemented")
    }
}