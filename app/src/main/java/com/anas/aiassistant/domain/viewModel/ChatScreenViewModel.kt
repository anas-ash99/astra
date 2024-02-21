package com.anas.aiassistant.domain.viewModel

import android.media.MediaPlayer
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
import com.anas.aiassistant.shared.StringValues
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import com.anas.aiassistant.ui.theme.TextSecondaryColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID

class ChatScreenViewModel:ViewModel() {


    private val repository:Repository = OpenAiImpl()
    var sendIconColor by mutableStateOf(SendIconNotClickableColor)
    var chatGbtLoading by mutableStateOf(false)
    var openChat by mutableStateOf( Chat())
    var messageTextInput by mutableStateOf("")
    var messageTexFieldColor by mutableStateOf(TextSecondaryColor) /// start with the secondary text
    var isTextFieldCardShown by mutableStateOf(false)
    var messageTextFieldFocus by mutableStateOf(false)

    var scrollPosition by mutableStateOf(0)


    fun onSendClick(){

        // create new user message and add it to the chat
        if (messageTextInput.trim().isNotBlank()){
            openChat.messages.add(Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = messageTextInput.trim(), role = "user", createdAt = ""))
            scrollPosition = openChat.messages.size -1
            getChatGbtResponse()
            restTextFieldValuesToDefault()
        }
    }



    private fun restTextFieldValuesToDefault(){
        // set the message text field to the default
        chatGbtLoading = true
        messageTextFieldFocus = false
        isTextFieldCardShown = false
        messageTexFieldColor = TextSecondaryColor
        messageTextInput = StringValues.text_field_hint_expanded
    }
     private fun getChatGbtResponse() {
        viewModelScope.launch {

            delay(500)
            val list = arrayListOf<ChatGBTMessage>()
            openChat.messages.forEach { msg ->
                list.add(ChatGBTMessage(role = msg.role, content = msg.content))
            }

            ///////////////////////////// simulation //////////////////
            val newMsg = Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = "Hello, how can i help you today? ", role = "system", createdAt = "")
            openChat.messages.add(newMsg)
            scrollPosition = openChat.messages.size -1
            chatGbtLoading = false

//            var chatRes: ChatCompletionRes
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

    fun onBackClick(navController: NavController?){
        messageTextFieldFocus = false
        messageTextInput = "Type, talk, or share a photo to Astra AI"
        if (isTextFieldCardShown){
            isTextFieldCardShown = false
        }else{
            navController?.popBackStack()
        }
    }
    fun readMessage(text:String) {
        viewModelScope.launch {
            repository.generateSpeechFromText(text).onEach {
                when(it){
                    is DataState.Success ->{
                        playAudio(it.data)
                        chatGbtLoading = false
                    }
                    is DataState.Error -> {
                        chatGbtLoading = false
                    }
                    DataState.Loading -> {
                        chatGbtLoading = true

                    }
                }
            }.launchIn(viewModelScope)

        }
    }
    private fun playAudio(tempFilePath:String) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(tempFilePath)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun setCurrentOpenChat(id:String) {
        openChat = chats.filter { it.id == id }[0]
        scrollPosition = openChat.messages.size -1
        if (openChat.messages.size == 1){ // check if this is the first message them get immediately chatgbt response
            getChatGbtResponse()
        }
    }

}