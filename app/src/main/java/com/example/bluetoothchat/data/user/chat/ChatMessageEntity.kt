package com.example.bluetoothchat.data.user.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.bluetoothchat.data.user.contact.ContactEntity
import com.example.bluetoothchat.domain.user.contact.Contact

@Entity(
    tableName = "message",
) data class ChatMessageEntity (
    @ColumnInfo("id")
    @PrimaryKey(true)
    val id: Int,

    @ColumnInfo("text")
    val text: String,

    @ColumnInfo("contactId")
    val contactId: Int,
){}