package com.example.bluetoothchat.di

import android.app.Application
import androidx.room.Room
import com.example.bluetoothchat.data.db.AppDatabase
import com.example.bluetoothchat.data.user.chat.ChatMessageDao
import com.example.bluetoothchat.data.user.chat.RoomChatMessageRepository
import com.example.bluetoothchat.data.user.contact.ContactDao
import com.example.bluetoothchat.domain.user.contact.ContactRepository
import com.example.bluetoothchat.data.user.contact.RoomContactRepository
import com.example.bluetoothchat.domain.user.chat.ChatMessageRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomDatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "contacts.db")
            .fallbackToDestructiveMigration(true)
            .build()

    @Provides
    fun provideContactDao(db: AppDatabase): ContactDao = db.contactDao()

    @Provides
    fun provideChatMessageDao(db: AppDatabase): ChatMessageDao = db.chatMessageDao()

    @Provides
    @Singleton
    fun provideContactRepository(dao: ContactDao): ContactRepository =
        RoomContactRepository(dao)

    @Provides
    @Singleton
    fun provideChatMessageRepository(dao: ChatMessageDao, contactRepository: ContactRepository): ChatMessageRepository =
        RoomChatMessageRepository(dao, contactRepository)
}