package com.anas.aiassistant.model

import android.util.Log
import com.anas.aiassistant.BuildConfig
import com.anas.aiassistant.data.RetrofitClient.apiService
import com.anas.aiassistant.dataState.DataState
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
    private val openAiApi: OpenAiApi,
): RemoteRepository {

    private val apiKey = BuildConfig.OPEN_AI_KEY

    override suspend fun getChatCompletion(list: ArrayList<ChatGBTMessage>, gbtModel:String): Flow<DataState<ChatCompletionRes>> = flow {
        emit(DataState.Loading)

        try {
            val request = CompletionRequest(
                model = gbtModel,
                messages = list
            )
            val res = openAiApi.generateCompletion(apiKey = "Bearer $apiKey", request = request)
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
            val res = apiService.textToSpeech("Bearer $apiKey", request)
            if (res.isSuccessful){

                // create an temp mp3 file that will be delete when application exist
                val inputStream = res.body()?.byteStream()
                val bytes = inputStream?.readBytes()
                val tempFile = File.createTempFile("speech", "mp3")
                println(tempFile)
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

}