package com.example.bluetoothchat.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

import com.example.bluetoothchat.data.user.contact.ContactEntity
import com.example.bluetoothchat.data.user.contact.ContactDao

@Database(entities = [ContactEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}

