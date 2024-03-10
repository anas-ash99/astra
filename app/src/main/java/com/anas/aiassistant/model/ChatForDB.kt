package com.anas.aiassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "chat")
data class ChatForDB(
    @PrimaryKey
    var id:String  = "",
    var title:String = "",
    var createdAt:String = "",
    var lastMessageTimestamp:String = ""
)
