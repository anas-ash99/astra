package com.anas.aiassistant.domain.viewModel

import android.media.MediaPlayer
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData.chats
import com.anas.aiassistant.data.OpenAiImpl
import com.anas.aiassistant.data.Repository
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.TextPrimaryColor
import com.anas.aiassistant.ui.theme.TextSecondaryColor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.util.UUID

class MainViewModel:ViewModel() {

    var sendIconColor by mutableStateOf(Color(0xB7747474))
    val suggestions = listOf("asd", "EASD", "awa", "A", "Rq")
    var chatGbtLoading by mutableStateOf(false)
    var mainScreenLoading by mutableStateOf(false)
    var openChat by mutableStateOf( Chat(title = "This is a test chat"))
    var messageTextInput by mutableStateOf(text_field_hint_expanded)
    var messageTexFieldColor by mutableStateOf(TextSecondaryColor) /// start with the secondary text
    var isTextFieldCardShown by mutableStateOf(false)
    var messageTextFieldFocus by mutableStateOf(false)
    private val repository:Repository = OpenAiImpl()
    val chatList = chats
    var scrollPosition by mutableStateOf(0)


    @RequiresApi(Build.VERSION_CODES.O)
    fun onSendClick(navController: NavController, chatId:String = ""){

        if (messageTextInput.trim().isNotBlank() && messageTextInput.trim() != text_field_hint_expanded){
            // if the message was sent from main, this means it s the first message in chat so we create new chat abd navigate to the chat screen
//            if (navController.currentBackStackEntry?.destination?.route == "main_screen"){
                val tempChat = Chat()
                tempChat.id = UUID.randomUUID().toString()
                tempChat.title = "testing something"
                tempChat.messages.add(Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = messageTextInput.trim(), role = "user", createdAt = LocalTime.now().toString()))
                chats.add(tempChat)
                navController.navigate("chat_screen/${tempChat.id}")
//                getChatGbtResponse(tempChat)
//            }else{
//                getChatGbtResponse(getChatById(chatId))
//            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getChatGbtResponse(tempChat: Chat) {
        viewModelScope.launch {
               // set the message text field to the default
                messageTextFieldFocus = false
                isTextFieldCardShown = false
                messageTexFieldColor = TextSecondaryColor
                messageTextInput = text_field_hint_expanded
                val list = arrayListOf<ChatGBTMessage>()
                tempChat.messages.forEach { msg ->
                    list.add(ChatGBTMessage(role = msg.role, content = msg.content))
                }


            // send the api request
            var chatRes: ChatCompletionRes
            repository.sentChatRequest(list).onEach {
                when(it){
                    is DataState.Success ->{
                        chatRes = it.data
                        val resMessage = chatRes.choices[0].message.content
                        val newMsg = Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = resMessage, role = "system", createdAt = LocalTime.now().toString())
                        tempChat.messages.add(newMsg)
                        //tempChat = tempChat
                        scrollPosition = tempChat.messages.size -1
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
    fun init(){
        getSuggestions()

    }

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

    private fun playAudio(tempFilePath:String) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(tempFilePath)
        mediaPlayer.prepare()
        mediaPlayer.start()
    }

    fun getChatById(id:String): Chat {
         return  chatList.filter { it.id == id }[0]
    }

    fun onSuggestionCardClick(text:String){
        messageTextFieldFocus = true
        messageTextInput = text
        messageTexFieldColor = TextPrimaryColor
    }
}


