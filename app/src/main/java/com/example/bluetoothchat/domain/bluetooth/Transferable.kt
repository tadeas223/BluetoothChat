package com.example.bluetoothchat.domain.bluetooth

interface Transferable<T> {
    fun serialize(value: T): ByteArray
    fun deserialize(data: ByteArray)
}