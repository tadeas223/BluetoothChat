package com.example.bluetoothchat.domain.bluetooth

import kotlin.reflect.KClass

interface Transferable<T: Any> {
    fun serialize(): ByteArray
    fun deserialize(data: ByteArray): T
}