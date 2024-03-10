package com.anas.aiassistant.shared

import androidx.navigation.NavController

sealed interface MainScreenEvent{
    data class OnSuggestionCardClick(val text:String):MainScreenEvent
    data class OnRecentChatClick(val id:String, val navController: NavController):MainScreenEvent
    data class OnRecentClick(val navController: NavController):MainScreenEvent
   object OnOkErrorDialogClick:MainScreenEvent
}