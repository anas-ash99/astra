package com.anas.aiassistant.model

data class Chat(
    var id:String  = "",
    var messages:ArrayList<Message> = arrayListOf(),
    var title:String = "",
    var createdAt:String = ""
)
