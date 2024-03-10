package com.anas.aiassistant.shared

import androidx.navigation.NavController

sealed interface BaseViewmodelEvents{
    data class SetMessageInput(val text:String):BaseViewmodelEvents
    data class OnSendClick(val navController: NavController?):BaseViewmodelEvents
    object OnMicClick:BaseViewmodelEvents
    object OnKeyboardIconClick:BaseViewmodelEvents

}
