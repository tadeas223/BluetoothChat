package com.example.bluetoothchat.domain.user.contact

import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun insert(contact: Contact)
    suspend fun insertAll(vararg contact: Contact)

    fun selectById(id: Int): Contact
    fun selectAll(): List<Contact>
    fun selectByName(name: String): Contact
    fun selectByAddress(address: String): Contact
    suspend fun update(contact: Contact)
    suspend fun delete(contact: Contact)
}