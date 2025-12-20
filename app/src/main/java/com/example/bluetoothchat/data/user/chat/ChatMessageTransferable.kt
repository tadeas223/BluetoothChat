package com.example.bluetoothchat.data.user.chat

import com.example.bluetoothchat.domain.bluetooth.Transferable
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

class ChatMessageTransferable(
    var value:ChatMessage
): Transferable<ChatMessage> {
    override fun serialize(): ByteArray {
        return Json.encodeToString(value).toByteArray(Charsets.UTF_8)
    }

    @OptIn(InternalSerializationApi::class)
    override fun deserialize(data: ByteArray): ChatMessage {
        value = Json.decodeFromString<ChatMessage>(data.toString(Charsets.UTF_8))
        return value
    }
}