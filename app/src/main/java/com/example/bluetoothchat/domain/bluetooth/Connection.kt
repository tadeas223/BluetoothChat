package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.Disposable
import kotlinx.coroutines.flow.Flow

interface Connection {
    val isConnected: Flow<Boolean>

    fun addReceiveListener(listener: ReceiveListener)
    fun removeReceiveListener(listener: ReceiveListener)

    fun addDisconnectListener(listener: DisconnectListener)
    fun removeDisconnectListener(listener: DisconnectListener)

    suspend fun disconnect()
    suspend fun <T: Any> send(transferable: Transferable<T>)
}