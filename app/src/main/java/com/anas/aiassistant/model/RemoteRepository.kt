package com.anas.aiassistant.model

import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import kotlinx.coroutines.flow.Flow

interface RemoteRepository {
    suspend fun getChatCompletion(list:ArrayList<ChatGBTMessage>, gbtModel:String):Flow<DataState<ChatCompletionRes>>
    suspend fun generateSpeechFromText(text:String):Flow<DataState<String>>  // return the path to the mp3 file



}