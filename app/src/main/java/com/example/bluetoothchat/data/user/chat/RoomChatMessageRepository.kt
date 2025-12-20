package com.example.bluetoothchat.data.user.chat

import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import com.example.bluetoothchat.domain.user.contact.Contact
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomChatMessageRepository @Inject constructor(
    val dao: ChatMessageDao,
    val contactRepository: ContactRepository
): ChatMessageRepository{
    override suspend fun insert(message: ChatMessage) {
        dao.insert(message.toEntity())
    }

    override suspend fun insertAll(vararg message: ChatMessage) {
        dao.insertAll(*message.map { it.toEntity() }.toTypedArray())
    }

    override fun selectById(id: Int): ChatMessage {
        return dao.selectById(id).toChatMessage(contactRepository)
    }

    override fun selectAll(): List<ChatMessage> {
        return dao.selectAll().map { it.toChatMessage(contactRepository) }
    }

    override fun selectByContact(sender: Contact): List<ChatMessage> {
        return dao.selectByContactId(sender.id).map {it.toChatMessage(contactRepository)}
    }

    override fun selectByContactFlow(sender: Contact): Flow<List<ChatMessage>> {
        return dao.selectByContactIdFlow(sender.id).map { list -> list.map {
            it.toChatMessage(contactRepository)
        }}
    }

    override suspend fun update(message: ChatMessage) {
        return dao.update(message.toEntity())
    }

    override suspend fun delete(message: ChatMessage) {
        return dao.delete(message.toEntity())
    }
}