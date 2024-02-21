package com.anas.aiassistant.domain.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.anas.aiassistant.data.OpenAiImpl
import com.anas.aiassistant.data.Repository
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import com.anas.aiassistant.ui.theme.TextSecondaryColor

open class BaseViewModel:ViewModel() {

    protected val repository: Repository = OpenAiImpl()

    var sendIconColor by mutableStateOf(SendIconNotClickableColor)
    var chatGbtLoading by mutableStateOf(false)
    var openChat by mutableStateOf( Chat())
    var messageTextInput by mutableStateOf("")
    var messageTexFieldColor by mutableStateOf(TextSecondaryColor) /// start with the secondary text
    var isTextFieldCardShown by mutableStateOf(false)
    var messageTextFieldFocus by mutableStateOf(false)
}