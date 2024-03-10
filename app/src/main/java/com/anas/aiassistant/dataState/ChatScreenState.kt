package com.anas.aiassistant.dataState

import com.anas.aiassistant.model.Chat

data class ChatScreenState(
    val openChat: Chat = Chat(),
    val scrollPosition:Int = 0,
    val isErrorDialogShown:Boolean = false
)
