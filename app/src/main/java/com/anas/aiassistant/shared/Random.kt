package com.anas.aiassistant.shared

import android.os.Build
import androidx.annotation.RequiresApi
import com.anas.aiassistant.model.Chat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


fun getLastThreeChats(list: ArrayList<Chat>): List<Chat> {
    if (list.size < 3) {
        return list  // Return the whole list if it has fewer than 3 elements
    }
    val startIndex = list.size - 3
    return list.subList(startIndex, list.size)
}