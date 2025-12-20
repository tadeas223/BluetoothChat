package com.example.bluetoothchat.di

import android.app.Application
import androidx.room.Room
import com.example.bluetoothchat.data.db.AppDatabase
import com.example.bluetoothchat.data.user.contact.ContactDao
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import com.example.bluetoothchat.data.user.contact.RoomContactRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "contacts.db").build()

    @Provides
    fun provideContactDao(db: AppDatabase): ContactDao = db.contactDao()

    @Provides
    @Singleton
    fun provideContactRepository(dao: ContactDao): ContactRepository =
        RoomContactRepository(dao)
}