package com.anas.aiassistant.model

import android.util.Log
import com.anas.aiassistant.data.AppData
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.domain.db.ChatDao
import com.anas.aiassistant.domain.db.MessageDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomDBRepo constructor(
    private val chatDao:ChatDao,
    private val messageDao:MessageDao
): DatabaseRepository {
    override suspend fun getAllChats(): Flow<DataState<ArrayList<ChatForDB>>> = flow {
        try {
            emit(DataState.Loading)
            val chats = ArrayList(chatDao.getAll().toList())

            AppData.chats2 = chats
            emit(DataState.Success(chats))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("get chats", e.message, e)
        }
    }

    override suspend fun saveMessage(message: Message): Flow<DataState<Message?>> = flow {
        try {
            emit(DataState.Loading)
            messageDao.insertAll(message)
            emit(DataState.Success(null))
        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("save message", e.message, e)
        }
    }

    override suspend fun saveChatToDB(chat: ChatForDB) {
        try {
            chatDao.insertAll(chat)
        }catch (e:Exception){
            Log.e("save chat", e.message, e)
        }
    }

    override suspend fun getChatById(chatId: String) = flow {
        try {
            emit(DataState.Loading)
            val chatForDB = chatDao.getChatById(chatId)
            val chat = Chat()
            val messages = messageDao.getMessagesByChatId(chatId)
            chat.id = chatForDB.id
            chat.title = chatForDB.title
            chat.createdAt = chatForDB.createdAt
            chat.messages = ArrayList(messages.toList())
            emit(DataState.Success(chat))

        }catch (e:Exception){
            emit(DataState.Error(e))
            Log.e("get chat by id", e.message, e)
        }
    }

    override suspend fun updateChat(chat: ChatForDB):Boolean {
        return try {
            chatDao.updateChat(chat)
            true
        }catch (e:Exception){
            Log.e("update chat", e.message, e)
            false
        }
    }

    override suspend fun deleteChatById(id: String):Boolean {
        return try {
            chatDao.deleteById(id)
            true
        }catch (e:Exception){
            Log.e("delete chat", e.message, e)
            false
        }
    }

}