package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.contact.Contact

interface ReceiveListener {
    fun onMessagePacket(packet: MessagePacket)
    fun onAdvertisePacket(packet: AdvertisePacket)
}