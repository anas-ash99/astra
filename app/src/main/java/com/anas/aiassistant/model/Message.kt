package com.anas.aiassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.anas.aiassistant.shared.MessageReadingState
import java.sql.Time
import java.time.LocalTime
import java.util.Date


@Entity
data class Message(
    @PrimaryKey
    val id:String,
    var chatId:String,
    var content:String,
    var role:String,
    var createdAt:String = LocalTime.now().toString(),
    var isContentLoading:Boolean = false,
    var readingState:String = MessageReadingState.INITIAL.value
)
