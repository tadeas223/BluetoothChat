package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.user.chat.ChatMessage
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

class AdvertisePacket (
    val address: String,
) {
    val type: String = "advertise"

    companion object {
        fun deserialize(json: JsonElement): ChatMessage  {
            return Json.decodeFromJsonElement<ChatMessage>(json)
        }
    }

    fun serialize(): JsonElement {
        return Json.encodeToJsonElement(this)
    }
}