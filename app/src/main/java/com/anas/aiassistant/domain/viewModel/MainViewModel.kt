package com.anas.aiassistant.domain.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData.chats
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.Repository
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
     private val context: Application
) :BaseViewModel(
    repository,
    context
) {


    var mainScreenLoading by mutableStateOf(false)
    var isErrorDialogShown by mutableStateOf(false)
    private var isInitialized = false



    fun init(){
        if (!isInitialized){ // run this code only once th
            getAllChats()
            isInitialized = true
        }
    }

    private fun getAllChats(){
        viewModelScope.launch {
            repository?.getAllChats()?.onEach {
                when(it){
                    is DataState.Error -> {
                        chatGbtLoading  = false
                    }
                    DataState.Loading -> {
//                        chatGbtLoading  = true
                    }
                    is DataState.Success -> {
                        chatList = it.data
                        chatGbtLoading  = false
                    }
                }
            }?.launchIn(viewModelScope)
        }
    }


    override fun onSendClick(navController: NavController?){

        if (messageTextInput.text.trim().isNotBlank() && messageTextInput.text.trim() != text_field_hint_expanded){
            val tempChat = Chat()
            tempChat.id = UUID.randomUUID().toString()
            if (messageTextInput.text.length > 28){
                tempChat.title = messageTextInput.text.take(28) + "..."
            }else{
                tempChat.title = messageTextInput.text
            }
            val newMessage = Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = messageTextInput.text.trim(), role = "user", createdAt = "")
            tempChat.messages.add(newMessage)
            chats.add(tempChat)
            saveMessageToDB(newMessage)
            navController?.navigate("chat_screen/${tempChat.id}")
            // set the message text field to the default
            restMessageTextFieldToDefault()
        }
    }

    private fun getSuggestions() {
        viewModelScope.launch {

            val content =
                   "Provide me with the 10 most popular diverse questions people would ask in any field around the world, and keep them concise.\n" +
                           "\n" +
                           "I need all questions in one line each question separated by , \n" +
                           "For example: how is your day , what is Metaverse"
            val list = arrayListOf<ChatGBTMessage>()
            list.add(ChatGBTMessage(role = "user", content = content))

            repository?.sentChatRequest(list)?.onEach {
                when(it){
                    is DataState.Error -> {
                        isErrorDialogShown = true
                        mainScreenLoading = false
                    }
                    DataState.Loading -> {
                        mainScreenLoading = true
                    }
                    is DataState.Success -> {
                        val resMessage = it.data.choices[0].message.content
                        Log.i("suggestions", resMessage)

                        mainScreenLoading = false
                    }
                }
            }?.launchIn(viewModelScope)
        }
    }

    fun onSuggestionCardClick(text:String){
        messageTextFieldFocus = true
        messageTextInput = TextFieldValue(text, TextRange(text.length))
        sendIconColor = SendIconClickableColor
    }
}


