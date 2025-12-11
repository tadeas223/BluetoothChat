package com.example.bluetoothchat.data.user.contact

import com.example.bluetoothchat.domain.user.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ContactRepository {
    suspend fun insert(contact: Contact)
    suspend fun insertAll(vararg contact: Contact)

    fun selectAll(): Flow<List<Contact>>
    fun selectByName(name: String): Contact
    fun selectByAddress(address: String): Contact
    suspend fun update(contact: Contact)
    suspend fun delete(contact: Contact)
}