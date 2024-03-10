package com.anas.aiassistant.domain.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.anas.aiassistant.model.ChatForDB
import com.anas.aiassistant.model.ChatWithMessages


@Dao
interface ChatDao {
    @Query("SELECT * FROM chat")
    suspend fun getAll(): List<ChatForDB>
    @Upsert
    suspend fun insertAll(chat: ChatForDB)
    @Query("SELECT * FROM chat WHERE id = :id")
    suspend fun getChatById(id:String): ChatForDB

    @Query("DELETE FROM chat WHERE id = :chatId")
    suspend fun deleteById(chatId: String)

    @Update
    suspend fun updateChat(chat: ChatForDB)


    //Prioritize Conversations: First, sort the chat conversations themselves based on when their last message was sent. The most recently active conversations will be at the top.
    // Order Messages Within Conversation: Secondly, within each conversation, sort the messages from oldest to newest.
    @Query("SELECT chat.*, message.* FROM chat JOIN message ON chat.id = message.chatId ORDER BY chat.lastMessageTimestamp DESC, message.createdAt ASC")
    suspend fun getConversationsWithMessages(): List<ChatWithMessages>
}