package com.example.bluetoothchat.domain.user.chat

import com.example.bluetoothchat.domain.user.contact.Contact
import kotlinx.coroutines.flow.Flow

interface ChatMessageRepository {
    suspend fun insert(message: ChatMessage)
    suspend fun insertAll(vararg message: ChatMessage)

    fun selectById(id: Int): ChatMessage
    fun selectAll(): List<ChatMessage>
    fun selectByContact(sender: Contact): List<ChatMessage>

    fun selectByContactFlow(sender: Contact): Flow<List<ChatMessage>>

    suspend fun update(message: ChatMessage)
    suspend fun delete(message: ChatMessage)
}