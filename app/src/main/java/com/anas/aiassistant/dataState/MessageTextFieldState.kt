package com.anas.aiassistant.dataState

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.shared.StringValues
import com.anas.aiassistant.ui.theme.AppMainColor
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor

data class MessageTextFieldState(
    var isTextFieldCardShown:Boolean =false,
    var chatList:ArrayList<Chat> = arrayListOf(),
    var sendIconColor: Color = SendIconNotClickableColor,
    var chatGbtLoading:Boolean =false,
    var messageTextInput:TextFieldValue = TextFieldValue(""),
    var messageTextInputHint:String = StringValues.text_field_hint_expanded,
    var messageTextFieldFocus:Boolean = false,
    var mediaContainerWidth:Dp = 130.dp,
    var bottomCardKeyBoardIconVisible:Boolean = false,
    var launchedEffectTFFocusTrigger:Int = 0,
    var micBackground:Color =  AppMainColor,
    var cursorPosition:Int = 0
)