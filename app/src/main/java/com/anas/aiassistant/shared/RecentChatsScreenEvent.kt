package com.anas.aiassistant.shared

import androidx.navigation.NavController

sealed class RecentChatsScreenEvent{
    data class OnDeleteItemClick(val id:String):RecentChatsScreenEvent()
    data class OnItemClick(val id:String, val navController: NavController):RecentChatsScreenEvent()
    data class OnPinItemClick(val id:String):RecentChatsScreenEvent()
    data class OnBackClick(val navController: NavController):RecentChatsScreenEvent()
}
