package com.example.bluetoothchat.data.user.chat

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.bluetoothchat.domain.user.chat.ChatMessage
import com.example.bluetoothchat.domain.user.contact.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: ChatMessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg message: ChatMessageEntity)


    @Update
    fun update(message: ChatMessageEntity)

    @Delete
    fun delete(message: ChatMessageEntity)

    @Query("SELECT * FROM message")
    fun selectAll(): List<ChatMessageEntity>

    @Query("SELECT * FROM message WHERE :id = id")
    fun selectById(id: Int): ChatMessageEntity

    @Query("SELECT * FROM message WHERE :id = contactId")
    fun selectByContactId(id: Int): List<ChatMessageEntity>

    @Query("SELECT * FROM message WHERE :id = contactId")
    fun selectByContactIdFlow(id: Int): Flow<List<ChatMessageEntity>>
}