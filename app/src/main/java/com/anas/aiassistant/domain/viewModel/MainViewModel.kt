package com.anas.aiassistant.domain.viewModel

import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData.chats
import com.anas.aiassistant.data.OpenAiImpl
import com.anas.aiassistant.data.Repository
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import com.anas.aiassistant.ui.theme.TextPrimaryColor
import com.anas.aiassistant.ui.theme.TextSecondaryColor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

class MainViewModel:ViewModel() {

    var chatList by mutableStateOf(chats)
    private val repository:Repository = OpenAiImpl()
    var sendIconColor by mutableStateOf(SendIconNotClickableColor)

    var mainScreenLoading by mutableStateOf(false)
    var messageTextInput by mutableStateOf(text_field_hint_expanded)
    var messageTexFieldColor by mutableStateOf(TextSecondaryColor) /// start with the secondary text
    var messageTextFieldFocus by mutableStateOf(false)
    var chatGbtLoading by mutableStateOf(false)



    fun onSendClick(navController: NavController){
        if (messageTextInput.trim().isNotBlank() && messageTextInput.trim() != text_field_hint_expanded){
            val tempChat = Chat()
            tempChat.id = UUID.randomUUID().toString()
            if (messageTextInput.length > 40){
                tempChat.title = messageTextInput.take(30) + "..."
            }else{
                tempChat.title = messageTextInput
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tempChat.messages.add(Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = messageTextInput.trim(), role = "user", createdAt = LocalTime.now().toString()))
            }else{
                tempChat.messages.add(Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = messageTextInput.trim(), role = "user", createdAt = ""))
            }
            chats.add(tempChat)
            navController.navigate("chat_screen/${tempChat.id}")
            // set the message text field to the default
            messageTextFieldFocus = false
            messageTexFieldColor = TextSecondaryColor
            messageTextInput = text_field_hint_expanded
            sendIconColor = SendIconNotClickableColor
        }
    }


//    private fun getChatGbtResponse(tempChat: Chat) {
//        viewModelScope.launch {
//               // set the message text field to the default
//                messageTextFieldFocus = false
//                isTextFieldCardShown = false
//                messageTexFieldColor = TextSecondaryColor
//                messageTextInput = text_field_hint_expanded
//                val list = arrayListOf<ChatGBTMessage>()
//                tempChat.messages.forEach { msg ->
//                    list.add(ChatGBTMessage(role = msg.role, content = msg.content))
//                }
//
//
//            // send the api request
//            var chatRes: ChatCompletionRes
//            repository.sentChatRequest(list).onEach {
//                when(it){
//                    is DataState.Success ->{
//                        chatRes = it.data
//                        val resMessage = chatRes.choices[0].message.content
//                        val newMsg = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = resMessage, role = "system", createdAt = LocalTime.now().toString())
//                        } else {
//                            Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = resMessage, role = "system", createdAt = "")
//                        }
//                        tempChat.messages.add(newMsg)
//                        //tempChat = tempChat
//                        scrollPosition = tempChat.messages.size -1
//                        chatGbtLoading = false
//                    }
//                    is DataState.Error -> {
//                        chatGbtLoading = false
//                    }
//                    DataState.Loading -> {
//                        chatGbtLoading = true
//                    }
//                }
//            }.launchIn(viewModelScope)
//        }
//    }


    private fun getSuggestions() {
        viewModelScope.launch {

            val list = arrayListOf<ChatGBTMessage>()
            list.add(ChatGBTMessage(role = "user", content = "what are the most popular prompts make them diverse"))

            repository.sentChatRequest(list).onEach {
                when(it){
                    is DataState.Error -> {
                        mainScreenLoading = false
                    }
                    DataState.Loading -> {
                        mainScreenLoading = true
                    }
                    is DataState.Success -> {
                        val resMessage = it.data.choices[0].message.content
                        println(resMessage)
                        mainScreenLoading = false
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    fun onSuggestionCardClick(text:String){
        messageTextFieldFocus = true
        messageTextInput = text
        messageTexFieldColor = TextPrimaryColor
        sendIconColor = SendIconClickableColor
    }
}


