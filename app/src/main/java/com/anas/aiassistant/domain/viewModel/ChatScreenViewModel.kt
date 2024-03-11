package com.anas.aiassistant.domain.viewModel


import android.annotation.SuppressLint
import android.app.Application
import android.media.MediaPlayer
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.model.DatabaseRepository
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.RemoteRepository
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.ChatScreenEvent
import com.anas.aiassistant.shared.MessageReadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val databaseRepository: DatabaseRepository,
    private val context: Application,
    speechRecognizer : SpeechRecognizer
) :BaseViewModel(
    context,
    databaseRepository,
    speechRecognizer
) {

    private var mediaPlayer = MediaPlayer()

    var scrollPosition by mutableStateOf(0)
    var openChat = MutableStateFlow(Chat())
    var isErrorDialogShown by mutableStateOf(false)
    var lazyColumnChangeTrigger by mutableStateOf(0)
    var scrollPositionTrigger by mutableStateOf(0)
    fun onScreenEvent(event:ChatScreenEvent){

        when(event){
            ChatScreenEvent.OnBottomChatCardClick -> openExtendedBottomCard()
            is ChatScreenEvent.OnArrowBackClick -> onBackClick(event.navController)
            is ChatScreenEvent.OnPauseIconClick -> stopAudio(event.messageId)
            is ChatScreenEvent.OnReadIconClick -> readMessage(event.messageContent, event.messageId)
        }
    }

    override fun onSendClick(navController: NavController?){
        // create new user message and add it to the chat
        if (textFieldState.value.messageTextInput.text.trim().isNotBlank()){
            val currentDateTime = ZonedDateTime.now()
            val formattedDateTime = currentDateTime.format(DateTimeFormatter.ISO_INSTANT)

            val customDateTime = ZonedDateTime.of(2024, 3, 8, 10, 30, 0, 0, ZoneId.systemDefault())
            val formattedCustomDateTime = customDateTime.format(DateTimeFormatter.ISO_INSTANT)
            val newMessage = Message(id = UUID.randomUUID().toString(), chatId = openChat.value.id, content = textFieldState.value.messageTextInput.text.trim(), role = "user", createdAt =formattedDateTime)
            openChat.value.messages.add(newMessage)
            scrollPosition = openChat.value.messages.size -1
            restMessageTextFieldToDefault()
            textFieldState.update { it.copy(
                isTextFieldCardShown = false
            ) }
            getChatGbtResponse()
            saveMessageToDB(newMessage)
            updateChatLastMessagesTimeStamp(newMessage.createdAt, openChat.value.id)
        }
    }

    private fun openExtendedBottomCard(){
        textFieldState.update {state -> state.copy(
            isTextFieldCardShown = true,
            messageTextInput = TextFieldValue("", TextRange(0)),
            launchedEffectTFFocusTrigger =+ 1,
            messageTextFieldFocus = true
        ) }
    }
     private fun getChatGbtResponse() {
         val currentDateTime = ZonedDateTime.now()
         val formattedDateTime = currentDateTime.format(DateTimeFormatter.ISO_INSTANT)

         val customDateTime = ZonedDateTime.of(2024, 3, 10, 11, 59, 0, 0, ZoneId.systemDefault())
         val formattedCustomDateTime = customDateTime.format(DateTimeFormatter.ISO_INSTANT)
         val list = arrayListOf<ChatGBTMessage>()
         openChat.value.messages.forEach { msg ->
             list.add(ChatGBTMessage(role = msg.role, content = msg.content))
         }

         viewModelScope.launch {
             val tempMessage = Message(id = UUID.randomUUID().toString(), chatId = openChat.value.id, content = "", role = "system", createdAt = formattedDateTime, isContentLoading = true)
             openChat.value.messages.add(tempMessage)
             lazyColumnChangeTrigger =+ 1
            var chatRes: ChatCompletionRes
            remoteRepository.getChatCompletion(list, "gpt-3.5-turbo").onEach {
                when(it){
                    is DataState.Success ->{
                        chatRes = it.data
                        val resMessage = chatRes.choices[0].message.content

                        displayChatResponse(resMessage, tempMessage.id)
                        saveMessageToDB(tempMessage)
                        updateChatLastMessagesTimeStamp(tempMessage.createdAt, openChat.value.id)
                        if (openChat.value.title.isBlank()) getChatTitle()
                    }
                    is DataState.Error -> {
                        isErrorDialogShown = true
                        openChat.value.messages.remove(tempMessage)
                        lazyColumnChangeTrigger += 1
                    }
                    DataState.Loading -> {
                         changeMessageLoadingStatus(tempMessage.id)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun setCurrentOpenChat(id:String) {
        viewModelScope.launch {
           databaseRepository.getChatById(id).onEach {
               when(it){
                   is DataState.Error -> {
                       isErrorDialogShown = true
                       Toast.makeText(context, "no chat with this id was found", Toast.LENGTH_SHORT).show()
                   }
                   DataState.Loading -> {

                   }
                   is DataState.Success -> {
                       openChat.value = it.data
                       lazyColumnChangeTrigger =+ 1
                       scrollPosition = openChat.value.messages.size -1
                       if (openChat.value.messages[openChat.value.messages.size -1].role == "user"){ // check if this is the last message was from the user then get chatgbt response
                           getChatGbtResponse()
                       }
                   }
               }
           }.launchIn(viewModelScope)
        }

    }


    private fun changeMessageLoadingStatus(messageId:String){
        openChat.value.messages.forEach { msg ->
            if (msg.id == messageId){
                msg.isContentLoading = true
            }
        }
        lazyColumnChangeTrigger += 1
    }
    fun onBackClick(navController: NavController?){
        if (textFieldState.value.isTextFieldCardShown){
            textFieldState.update { it.copy(
                isTextFieldCardShown = false
            ) }
            restMessageTextFieldToDefault()
        }else{
            restMessagesReadStateToDefault()
            if (mediaPlayer.isPlaying){
                stopAudio()
            }
            navController?.popBackStack()
        }
    }

    private fun restMessagesReadStateToDefault(){
        openChat.value.messages.forEach { msg ->
            msg.readingState = MessageReadingState.READY.value
            if (msg.isContentLoading){
                openChat.value.messages.remove(msg)
            }
        }
    }
    private fun readMessage(text:String, messageId:String) {
        viewModelScope.launch {
            remoteRepository.generateSpeechFromText(text).onEach { res->
                when(res){
                    is DataState.Success ->{
                        playAudio(res.data, messageId)
                    }
                    is DataState.Error -> {
                        isErrorDialogShown = true
                    }
                    DataState.Loading -> {
                        updateMessageReadingState(MessageReadingState.LOADING.value, messageId)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


    //
    private fun playAudio(tempFilePath:String, messageId: String) {

        try {
            if (mediaPlayer.isPlaying){
                stopAudio()
                restMessagesReadStateToDefault()
            }
            updateMessageReadingState(MessageReadingState.READING.value, messageId)
            mediaPlayer.setDataSource(tempFilePath)
            mediaPlayer.prepare()
            mediaPlayer.start()
            mediaPlayer.setOnCompletionListener {
                updateMessageReadingState(MessageReadingState.READY.value, messageId)
                mediaPlayer.release()
                mediaPlayer = MediaPlayer()

            }

        }catch (e:IOException){
             isErrorDialogShown = true
             Log.e("play audio", e.message, e)
        }
    }

    private fun stopAudio(messageId: String = "" ){
        try {
            mediaPlayer.stop()
            mediaPlayer = MediaPlayer()
            if (messageId.isNotBlank())
                updateMessageReadingState(MessageReadingState.READY.value, messageId)

            lazyColumnChangeTrigger += 1
        }catch (e:Exception){
            isErrorDialogShown = true
            Log.e("stop audio", e.message, e)
        }
    }

    private suspend fun displayChatResponse(text: String, messageId: String){
        text.withIndex().forEach{(index,  messageLetter)->
            withContext(Dispatchers.Main) {
            openChat.value.messages.forEach { msg ->
                if (msg.id == messageId){
                    if (msg.isContentLoading){  // change the loading status only once
                        msg.isContentLoading = false
                    }
                    msg.content += messageLetter
                    msg.readingState = MessageReadingState.READY.value
                }
            }

                if(index % 3 == 0){  // add a delay every 3 characters
                    delay(1)
                }
                scrollPositionTrigger += 1
            }
        }

    }

    private suspend fun animateChatTitleChange(title:String){
        title.forEach{titleLetter->
            withContext(Dispatchers.Main){
                openChat.update { chat ->
                    chat.copy(
                        title = chat.title + titleLetter
                    )
                }
            }
            delay(1)

        }
    }
    private fun updateMessageReadingState(state:String, messageId:String){
        openChat.value.messages.forEach { msg ->
            if (msg.id == messageId){
                msg.readingState = state
            }
        }
        openChat.value.messages = openChat.value.messages
        lazyColumnChangeTrigger += 1
    }


    private fun getChatTitle(){
        viewModelScope.launch {
            val list = arrayListOf<ChatGBTMessage>()

            var messageContent =  "Analyze this older conversation between me and you and capture the tone: "
            messageContent += "\n user: ${openChat.value.messages[0].content}"
            messageContent += "\n You: ${openChat.value.messages[1].content}"
            messageContent += "\n Create the shortest possible title that still conveys the basic topic. Maximum 25 letters. Do not include any quotation marks in the title "

            list.add(ChatGBTMessage(role = "user", content = messageContent))
            remoteRepository.getChatCompletion(list, "gpt-4").onEach {
                when(it){
                    is DataState.Error -> {
                        Toast.makeText(context, "Error getting chat title", Toast.LENGTH_SHORT).show()
                    }
                    DataState.Loading -> {}
                    is DataState.Success -> {
                        val chatRes = it.data
                        var resMessage = chatRes.choices[0].message.content.trim()
                        resMessage.replace(Regex("^\"|\"$"), "") // remove any extra double quote
                        if (resMessage.length > 28){ // in case the title is to ling cut down to 28 characters
                           resMessage =  resMessage.take(28) + "..."
                        }
                        animateChatTitleChange(resMessage)
                        updateChatTitleInDb(resMessage)

                    }
                }
            }.launchIn(viewModelScope)
        }

    }

    private suspend fun updateChatTitleInDb(title:String){
        AppData.chats.forEach { chat ->
            if (chat.id== openChat.value.id) {
                chat.title = title
                databaseRepository.updateChat(chat)
            }
        }
    }



}