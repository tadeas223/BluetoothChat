package com.example.bluetoothchat.data.user.chat

import com.example.bluetoothchat.data.user.contact.toContact
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import com.example.bluetoothchat.domain.user.contact.ContactRepository

fun ChatMessageEntity.toChatMessage(contactRepository: ContactRepository): ChatMessage {
    return ChatMessage(
        id = this.id,
        sender = contactRepository.selectById(this.senderId),
        text = this.text,
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        id = this.id,
        senderId = this.sender.id,
        text = this.text,
    )
}
