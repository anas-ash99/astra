package com.anas.aiassistant.model.openAi

import com.anas.aiassistant.model.Message

data class Choice(
    val index: Int,
    val message: Message,
    val logprobs: Any?,
    val finishReason: String
)
