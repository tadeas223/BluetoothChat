package com.example.bluetoothchat.domain.bluetooth

import com.example.bluetoothchat.domain.Disposable
import kotlinx.coroutines.flow.Flow

interface Connection : Disposable {
    val isConnected: Boolean

    fun addListener(listener: ConnectionListener)
    fun removeListener(listener: ConnectionListener)

    fun addDisconnectListener(listener: DisconnectListener)
    fun removeDisconnectListener(listener: DisconnectListener)

    suspend fun disconnect()
    suspend fun send(transferable: Transferable<*>)
}