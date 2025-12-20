package com.example.bluetoothchat.domain.user.contact

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Contact (
    @Transient
    val id: Int = 0,

    val username: String,
    val address: String,
) {}