package com.anas.aiassistant.model

import android.util.Log
import com.anas.aiassistant.data.ApiKeys.OPEN_AI_API_KEY
import com.anas.aiassistant.data.AppData
import com.anas.aiassistant.data.RetrofitClient.apiService
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.domain.viewModel.AppDatabase
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.model.openAi.CompletionRequest
import com.anas.aiassistant.model.openAi.TTSRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class OpenAiImpl @Inject constructor(
    private val db:AppDatabase,
    private val openAiApi: OpenAiApi
): Repository {



    override suspend fun sentChatRequest(list: ArrayList<ChatGBTMessage>): Flow<DataState<ChatCompletionRes>> = flow {
        emit(DataState.Loading)

        try {
            val request = CompletionRequest(
                model = "gpt-3.5-turbo",
                messages = list
            )
            val res = openAiApi.generateCompletion(apiKey = "Bearer $OPEN_AI_API_KEY", request = request)
            emit(DataState.Success(res))
        }catch (e:Exception){
            Log.e("Error sentChatRequest ", e.message, e)
            emit(DataState.Error(e))
        }

    }

    override suspend fun generateSpeechFromText(text: String): Flow<DataState<String>> = flow {

        emit(DataState.Loading)
        try {
            var audioFilePath = "";
            val request = TTSRequest(
                "tts-1",
                text,
                "alloy"
            )
            val res = apiService.textToSpeech("Bearer $OPEN_AI_API_KEY", request)
            if (res.isSuccessful){

                // create an temp mp3 file that will be delete when application exist
                val inputStream = res.body()?.byteStream()
                val bytes = inputStream?.readBytes()
                val tempFile = File.createTempFile("speech", "mp3")
                tempFile.deleteOnExit()
                val fos = FileOutputStream(tempFile)
                fos.write(bytes)
                fos.close()
                audioFilePath = tempFile.path
                emit(DataState.Success(audioFilePath))
            }else{
                throw Exception("Api fail")
            }
        }catch (e:Exception){
            Log.e("Error generateTextFromSpeech ", e.message, e)
            emit(DataState.Error(e))
        }

    }

    override suspend fun getAllChats(): Flow<DataState<ArrayList<Chat>>> = flow {
        emit(DataState.Loading)
        try {
           val messages = db?.messageDao()?.getAll()
            val chatList = arrayListOf<Chat>()
            messages?.forEach { msg->
                val tempChat:Chat? = chatList.find { it.id == msg.chatId }
                if (tempChat == null){ // create new chat
                    val msgTitle =  if (msg.content.length > 28) msg.content.take(28) + "..." else msg.content

                    val newChat = Chat(id = msg.chatId, title = msgTitle)
                    newChat.messages.add(msg)
                    chatList.add(newChat)
                }else{ //add the message to the matching existing chat
                    tempChat.messages.add(msg)
                }
            }
            AppData.chats = chatList
            emit(DataState.Success(chatList))
        }catch (e:Exception){
            Log.e("Error getAllChats ", e.message, e)
            emit(DataState.Error(e))
        }
    }

    override suspend fun saveMessages(vararg messages: Message): Flow<DataState<Message?>> = flow {
        emit(DataState.Loading)
        try {
           db?.messageDao()?.insertAll(*messages)
            emit(DataState.Success(null))
        }catch (e:Exception){
            Log.e("Error getAllChats ", e.message, e)
            emit(DataState.Error(e))
        }
    }

}