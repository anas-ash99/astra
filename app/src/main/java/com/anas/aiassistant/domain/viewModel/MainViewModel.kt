package com.anas.aiassistant.domain.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData.chats2
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.dataState.MainScreenState
import com.anas.aiassistant.model.ChatForDB
import com.anas.aiassistant.model.DatabaseRepository
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.RemoteRepository
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.MainScreenEvent
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject


@SuppressLint("StaticFieldLeak")
@HiltViewModel
class MainViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val context: Application,
    private val databaseRepository: DatabaseRepository,
    speechRecognizer : SpeechRecognizer
) :BaseViewModel(
    remoteRepository,
    context,
    databaseRepository,
    speechRecognizer
) {

    private var isInitialized = false
    val state = MutableStateFlow(MainScreenState())

    fun onEvent(event: MainScreenEvent){
        when(event){
            is MainScreenEvent.OnRecentChatClick -> {}
            is MainScreenEvent.OnRecentClick -> {event.navController.navigate("recent_chats_screen")}
            is MainScreenEvent.OnSuggestionCardClick -> onSuggestionCardClick(event.text)
            MainScreenEvent.OnOkErrorDialogClick -> {
                state.update { state -> state.copy(
                    isErrorDialogShown = false
                ) }
            }
        }
    }


    fun init(){
        if (!isInitialized){ // run this code only once th
            getAllChats()
            isInitialized = true
        }
    }

    private fun getAllChats(){
        viewModelScope.launch {
            databaseRepository.getAllChats().onEach {
                when(it){
                    is DataState.Error -> {
                     Toast.makeText(context, "Error retrieving chats from local database", Toast.LENGTH_SHORT).show()
                    }
                    DataState.Loading -> {
//                        chatGbtLoading  = true
                    }
                    is DataState.Success -> {
                        chatList = it.data
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    override fun onSendClick(navController: NavController?){

        if (textFieldState.value.messageTextInput.text.trim().isNotBlank() && textFieldState.value.messageTextInput.text.trim() != text_field_hint_expanded){
            val currentDateTime = ZonedDateTime.now()
            val formattedDateTime = currentDateTime.format(DateTimeFormatter.ISO_INSTANT)

            /// create custom date time
//            val customDateTime = ZonedDateTime.of(2023, 1, 29, 10, 30, 0, 0, ZoneId.systemDefault())
//            val formattedCustomDateTime = customDateTime.format(DateTimeFormatter.ISO_INSTANT)
            val tempChat = ChatForDB()

            tempChat.id = UUID.randomUUID().toString()
            tempChat.createdAt = formattedDateTime

//            if (textFieldState.value.messageTextInput.text.length > 28){
//                tempChat.title = textFieldState.value. messageTextInput.text.take(28) + "..."
//            }else{
//                tempChat.title = textFieldState.value.messageTextInput.text
//            }
            val newMessage = Message(id = UUID.randomUUID().toString(), chatId = tempChat.id, content = textFieldState.value.messageTextInput.text.trim(), role = "user", createdAt = formattedDateTime)
//            tempChat.messages.add(newMessage)
            tempChat.lastMessageTimestamp = newMessage.createdAt
            chats2.add(tempChat)
            saveMessageToDB(newMessage)
            saveChatToDb(tempChat)

            navController?.navigate("chat_screen/${tempChat.id}")
            // set the message text field to the default
            restMessageTextFieldToDefault()
        }
    }
  private fun saveChatToDb(chat: ChatForDB){
      viewModelScope.launch {
          databaseRepository.saveChatToDB(chat)
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

            remoteRepository.getChatCompletion(list).onEach {
                when(it){
                    is DataState.Error -> {
                        state.update { state -> state.copy(
                            isScreenLoading = false,
                            isErrorDialogShown = true
                        ) }
                    }
                    DataState.Loading -> {
                        state.update { state -> state.copy(
                            isScreenLoading = true
                        ) }

                    }
                    is DataState.Success -> {
                        val resMessage = it.data.choices[0].message.content
                        Log.i("suggestions", resMessage)
                        state.update { state -> state.copy(
                            isScreenLoading = false
                        ) }

                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onSuggestionCardClick(text:String){
        textFieldState.update { it.copy(
            messageTextFieldFocus = true,
            messageTextInput = TextFieldValue(text, TextRange(text.length)),
            sendIconColor = SendIconClickableColor
        ) }
//        messageTextFieldFocus = true
//        messageTextInput = TextFieldValue(text, TextRange(text.length))
//        sendIconColor = SendIconClickableColor
    }
}


