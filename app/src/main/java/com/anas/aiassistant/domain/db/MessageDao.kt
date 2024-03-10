package com.anas.aiassistant.domain.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.anas.aiassistant.model.Message


@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    suspend fun getAll(): List<Message>

    @Query("SELECT * FROM message Where chatId = :chatId")
    suspend fun getMessagesByChatId(chatId:String): List<Message>
    @Upsert
    suspend fun insertAll(vararg messages: Message)
}