package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.user.chat.ChatMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

data class MessagePacket (
    val message: String
){
    val type: String = "message"

    companion object {
        fun deserialize(json: JsonElement): ChatMessage  {
            return Json.decodeFromJsonElement<ChatMessage>(json)
        }
    }

    fun serialize(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}