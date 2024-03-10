package com.anas.aiassistant.model

import com.anas.aiassistant.dataState.DataState
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {

    suspend fun getAllChats(): Flow<DataState<ArrayList<ChatForDB>>>
    suspend fun saveMessage(messages: Message): Flow<DataState<Message?>>
    suspend fun saveChatToDB(chat: ChatForDB)
    suspend fun getChatById(chatId: String): Flow<DataState<Chat>>
    suspend fun updateChat(chat:ChatForDB):Boolean
    suspend fun deleteChatById(id:String):Boolean
}