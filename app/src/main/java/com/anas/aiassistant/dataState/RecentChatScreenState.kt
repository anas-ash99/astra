package com.anas.aiassistant.dataState

import com.anas.aiassistant.model.Chat

data class RecentChatScreenState(
    val chats:List<Chat> = emptyList(),
    val isContextMenuShown:Boolean = false
)
