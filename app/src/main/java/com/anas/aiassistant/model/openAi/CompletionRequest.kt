package com.anas.aiassistant.model.openAi

data class CompletionRequest(
    val model: String,
    val messages: List<ChatGBTMessage>
)
