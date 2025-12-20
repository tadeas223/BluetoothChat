package com.example.bluetoothchat.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bluetoothchat.data.user.chat.ChatMessageDao
import com.example.bluetoothchat.data.user.chat.ChatMessageEntity

import com.example.bluetoothchat.data.user.contact.ContactEntity
import com.example.bluetoothchat.data.user.contact.ContactDao

@Database(entities = [ContactEntity::class, ChatMessageEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
    abstract fun chatMessageDao(): ChatMessageDao
}

