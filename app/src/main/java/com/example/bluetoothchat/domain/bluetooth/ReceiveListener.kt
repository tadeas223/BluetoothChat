package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.user.chat.ChatMessage

interface ReceiveListener {
    fun onReceive(message: ChatMessage)
}