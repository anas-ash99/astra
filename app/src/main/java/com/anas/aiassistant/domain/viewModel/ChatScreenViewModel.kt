package com.anas.aiassistant.domain.viewModel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.aiassistant.data.AppData.chats
import com.anas.aiassistant.data.OpenAiImpl
import com.anas.aiassistant.data.Repository
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.StringValues
import com.anas.aiassistant.ui.theme.TextSecondaryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

class ChatScreenViewModel:ViewModel() {


    private val repository:Repository = OpenAiImpl()
    var sendIconColor by mutableStateOf(Color(0xB7747474))
    var chatGbtLoading by mutableStateOf(false)
    var openChat by mutableStateOf( Chat())
    var messageTextInput by mutableStateOf(StringValues.text_field_hint_expanded)
    var messageTexFieldColor by mutableStateOf(TextSecondaryColor) /// start with the secondary text
    var isTextFieldCardShown by mutableStateOf(false)
    var messageTextFieldFocus by mutableStateOf(false)

    val chatList = arrayListOf<Chat>()
    var scrollPosition by mutableStateOf(0)



    @RequiresApi(Build.VERSION_CODES.O)
    fun onSendClick(){
        chatGbtLoading = true
        openChat.messages.add(Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = messageTextInput.trim(), role = "user", createdAt = LocalTime.now().toString()))
        chatList.add(openChat)
        openChat = openChat
         getChatGbtResponse()
    }

    @RequiresApi(Build.VERSION_CODES.O)
     fun getChatGbtResponse() {
        viewModelScope.launch {
            // set the message text field to the default
            messageTextFieldFocus = false
            isTextFieldCardShown = false
            messageTexFieldColor = TextSecondaryColor
            messageTextInput = StringValues.text_field_hint_expanded
            val list = arrayListOf<ChatGBTMessage>()
            openChat.messages.forEach { msg ->
                list.add(ChatGBTMessage(role = msg.role, content = msg.content))
            }

             delay(1500)
            val newMsg = Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = "Hi im happy to chat with you", role = "system", createdAt = LocalTime.now().toString())
            openChat.messages.add(newMsg)
            chatGbtLoading = false
            openChat = openChat
            // send the api request
            var chatRes: ChatCompletionRes
//            repository.sentChatRequest(list).onEach {
//                when(it){
//                    is DataState.Success ->{
//                        chatRes = it.data
//                        val resMessage = chatRes.choices[0].message.content
//                        val newMsg = Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = resMessage, role = "system", createdAt = LocalTime.now().toString())
//                        openChat.messages.add(newMsg)
//                        //tempChat = tempChat
//                        scrollPosition = openChat.messages.size -1
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
        }
    }

    fun getChatById(id:String): Chat {
        return  chats.filter { it.id == id }[0]
    }

}