package com.anas.aiassistant.shared

import android.speech.SpeechRecognizer
import androidx.navigation.NavController

sealed interface BaseViewmodelEvents{
    data class SetMessageInput(val text:String):BaseViewmodelEvents
    data class OnSendClick(val navController: NavController?):BaseViewmodelEvents
    data class OnMicClick(val speechRecognizer:SpeechRecognizer):BaseViewmodelEvents
    object OnKeyboardIconClick:BaseViewmodelEvents
}
