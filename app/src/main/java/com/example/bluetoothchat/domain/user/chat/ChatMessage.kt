package com.example.bluetoothchat.domain.user.chat

import com.example.bluetoothchat.domain.user.contact.Contact
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ChatMessage(
    @Transient
    val id: Int = 0,
    val text: String,
    val contact: Contact,
)