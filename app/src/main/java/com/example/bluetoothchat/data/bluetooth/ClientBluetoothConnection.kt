package com.example.bluetoothchat.data.bluetooth

import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.compose.runtime.MutableState
import com.example.bluetoothchat.domain.bluetooth.AdvertisePacket
import com.example.bluetoothchat.domain.bluetooth.Connection
import com.example.bluetoothchat.domain.bluetooth.DisconnectListener
import com.example.bluetoothchat.domain.bluetooth.MessagePacket
import com.example.bluetoothchat.domain.bluetooth.ReceiveListener
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonToken
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeToSequence
import kotlinx.serialization.json.encodeToJsonElement
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class ClientBluetoothConnection : Connection {
    private val mapper = ObjectMapper()
    private val factory = JsonFactory()
    private var chatMessageRepository: ChatMessageRepository
    private var scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private var socket: BluetoothSocket? = null
    override var address: String? = null

    private val _isConnected =  MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean>
        get() = _isConnected

    private val disconnectListeners = mutableSetOf<DisconnectListener>()
    private val receiveListeners = mutableSetOf<ReceiveListener>()

    constructor(chatMessageRepository: ChatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository
    }

    constructor(socket: BluetoothSocket, chatMessageRepository: ChatMessageRepository) {
        _isConnected.value = socket.isConnected
        this.chatMessageRepository = chatMessageRepository

        if(_isConnected.value) {
            receive()
            this.socket = socket
            address = socket.remoteDevice.address
        }
    }

    fun receive() {
        Log.d("BluetoothChat", "receive()")
        scope.launch {
            val socket = socket ?: run {
                disconnect()
                return@launch
            }

            try {
                val parser = factory.createParser(socket.inputStream)

                while (true) {
                    val token = parser.nextToken() ?: break

                    if (token != JsonToken.START_OBJECT) {
                        continue
                    }

                    val map: Map<String, Any?> =
                        mapper.readValue(
                            parser,
                            object : TypeReference<Map<String, Any?>>() {}
                        )

                    when (map["type"]) {
                        "message" -> {
                            val message = map["message"] as? String ?: continue
                            val packet = MessagePacket(message)
                            callReceiveListenerOnMessage(packet)
                        }
                        "advertise" -> {
                            val address = map["address"] as? String ?: continue
                            val packet = AdvertisePacket(address)
                            callReceiveListenerOnAdvertise(packet)
                        }
                    }
                }
            } catch (e: IOException) {
                Log.d("BluetoothChat", "receive failed", e)
                disconnect()
            }
        }
    }
    suspend fun connectToSocket(socket: BluetoothSocket) {
        try {
            socket.connect()
        } catch (_: IOException) {
            _isConnected.value = false
            callDisconnectListeners()
            return
        }
        this.socket = socket
        _isConnected.value = socket.isConnected
        address = socket.remoteDevice.address
        receive()
    }

    override fun addDisconnectListener(listener: DisconnectListener) {
        disconnectListeners.add(listener)
    }

    override fun removeDisconnectListener(listener: DisconnectListener) {
        disconnectListeners.remove(listener)
    }

    override fun addReceiveListener(listener: ReceiveListener) {
        receiveListeners.add(listener)
    }

    override fun removeReceiveListener(listener: ReceiveListener) {
        receiveListeners.remove(listener)
    }

    override suspend fun disconnect() {
        socket?.close()
        socket = null
        _isConnected.value = false
        callDisconnectListeners()

        scope.cancel()
    }

    override suspend fun send(message: ChatMessage) {
        val serialized = message.serialize().toString().toByteArray(Charsets.UTF_8)
        val length = serialized.size

        if(socket == null || socket!!.outputStream == null) return

        val outputStream = DataOutputStream(socket!!.outputStream!!)

        try {
            outputStream.write(serialized)
            Log.d("BluetoothChat", "message sent to ${socket!!.remoteDevice.address}")
        } catch(_: IOException) {
            Log.d("BluetoothChat", "failed to send, disconnecting ${socket!!.remoteDevice.address}")
            disconnect()
        }

    }

    private fun callDisconnectListeners() {
        for (listener in disconnectListeners) {
            listener.onDisconnected(this)
        }
    }

    private fun callReceiveListenerOnMessage(message: MessagePacket) {
        for (listener in receiveListeners) {
            listener.onMessagePacket(message)
        }
    }

    private fun callReceiveListenerOnAdvertise(packet: AdvertisePacket) {
        for (listener in receiveListeners) {
            listener.onAdvertisePacket(packet)
        }
    }
}