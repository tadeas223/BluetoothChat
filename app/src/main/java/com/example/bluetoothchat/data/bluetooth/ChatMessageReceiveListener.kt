package com.example.bluetoothchat.data.bluetooth

import com.example.bluetoothchat.domain.bluetooth.ReceiveListener
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository

class ChatMessageReceiveListener(
    val receive: (message: ChatMessage) -> Unit
): ReceiveListener {
    override fun onReceive(message: ChatMessage) {
        receive(message)
    }
}