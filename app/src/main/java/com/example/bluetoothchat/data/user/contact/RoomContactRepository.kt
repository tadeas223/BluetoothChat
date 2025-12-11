package com.example.bluetoothchat.data.user.contact

import androidx.room.Query
import com.example.bluetoothchat.domain.user.Contact
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RoomContactRepository @Inject constructor(
    private val dao: ContactDao
): ContactRepository {
    override suspend fun insert(contact: Contact) {
        dao.insert(contact.toEntity())
    }

    override fun selectAll(): Flow<List<Contact>> {
        return dao.selectAll()
    }

    override suspend fun insertAll(vararg contact: Contact) {
        dao.insertAll(*contact.map { it.toEntity() }.toTypedArray())
    }

    override fun selectByName(name: String): Contact {
        return dao.selectByName(name).toContact()
    }

    override fun selectByAddress(address: String): Contact {
        return dao.selectByAddress(address).toContact()
    }

    override suspend fun update(
        contact: Contact
    ) {
        dao.update(contact.toEntity())
    }

    override suspend fun delete(contact: Contact) {
        dao.delete(contact.toEntity())
    }

}