package com.anas.aiassistant.model

import androidx.room.Embedded
import androidx.room.Relation

data class ChatWithMessages (
    @Embedded val chat: ChatForDB,
    @Relation(parentColumn = "id", entityColumn = "chatId")
    var messages:List<Message> = arrayListOf(),
)