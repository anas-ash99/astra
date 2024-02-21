package com.anas.aiassistant.data

import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun sentChatRequest(list:ArrayList<ChatGBTMessage>):Flow<DataState<ChatCompletionRes>>
    suspend fun generateSpeechFromText(text:String):Flow<DataState<String>>  // return the path to the mp3 file
}