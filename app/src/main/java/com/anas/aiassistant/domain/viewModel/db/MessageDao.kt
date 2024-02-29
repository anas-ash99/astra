package com.anas.aiassistant.domain.viewModel.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.anas.aiassistant.model.Message


@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    suspend fun getAll(): List<Message>
    @Insert
    suspend fun insertAll(vararg messages: Message)
}