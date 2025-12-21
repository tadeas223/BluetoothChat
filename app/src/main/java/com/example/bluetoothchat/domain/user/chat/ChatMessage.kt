package com.example.bluetoothchat.domain.user.chat

import com.example.bluetoothchat.domain.user.contact.Contact
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class ChatMessage(
    @Transient
    val id: Int = 0,
    val text: String,
    @Transient
    val contact: Contact? = null,
) {
    companion object {
        fun deserialize(json: JsonElement): ChatMessage  {
            return Json.decodeFromJsonElement<ChatMessage>(json)
        }
    }

    fun serialize(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}