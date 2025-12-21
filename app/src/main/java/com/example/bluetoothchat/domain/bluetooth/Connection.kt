package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.Disposable
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface Connection {
    val isConnected: StateFlow<Boolean>
    val address: String?
    fun addDisconnectListener(listener: DisconnectListener)
    fun removeDisconnectListener(listener: DisconnectListener)

    fun addReceiveListener(listener: ReceiveListener)
    fun removeReceiveListener(listener: ReceiveListener)

    suspend fun disconnect()
    suspend fun send(message: ChatMessage)
}