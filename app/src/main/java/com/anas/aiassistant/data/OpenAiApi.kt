package com.anas.aiassistant.data

import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.CompletionRequest
import com.anas.aiassistant.model.openAi.TTSRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("chat/completions")
    suspend fun generateCompletion(
        @Header("Authorization") apiKey: String,
        @Body request: CompletionRequest
    ):ChatCompletionRes

    @POST("audio/speech")
    suspend fun textToSpeech(
        @Header("Authorization") apiKey: String,
        @Body request: TTSRequest
    ):Response<ResponseBody>
}