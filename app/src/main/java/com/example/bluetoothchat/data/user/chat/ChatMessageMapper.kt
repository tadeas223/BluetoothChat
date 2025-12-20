package com.example.bluetoothchat.data.user.chat

import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

suspend fun ChatMessageEntity.toChatMessage(contactRepository: ContactRepository): ChatMessage {
    return ChatMessage(
        id = this.id,
        contact = contactRepository.selectById(this.contactId).first(),
        text = this.text,
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        id = this.id,
        contactId = this.contact.id,
        text = this.text,
    )
}
