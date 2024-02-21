package com.anas.aiassistant.model.openAi

data class ChatCompletionRes(
    val id: String,
    val `object`: String,
    val created: Long,
    val model: String,
    val systemFingerprint: String,
    val choices: List<Choice>,
    val usage: Usage
)
