package com.anas.aiassistant.model

data class Message(
    val id:String,
    var chatId:String,
    var content:String,
    var role:String,
    var createdAt:String
)
