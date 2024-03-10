package com.anas.aiassistant.shared

import androidx.navigation.NavController

sealed class ChatScreenEvent{
    object OnBottomChatCardClick: ChatScreenEvent()
    data class OnReadIconClick(val messageContent:String,val messageId:String): ChatScreenEvent()
    data class OnPauseIconClick(val messageId:String): ChatScreenEvent()
    data class OnArrowBackClick(val navController: NavController): ChatScreenEvent()

}
