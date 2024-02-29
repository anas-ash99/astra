package com.anas.aiassistant.domain.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData.chats
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.Chat
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.Repository
import com.anas.aiassistant.model.openAi.ChatCompletionRes
import com.anas.aiassistant.model.openAi.ChatGBTMessage
import com.anas.aiassistant.shared.MessageReadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.UUID
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ChatScreenViewModel @Inject constructor(
    private val repository: Repository,
  private val context: Application
) :BaseViewModel(
    repository,
    context
) {

    var scrollPosition by mutableStateOf(0)
    var isTextFieldCardShown by mutableStateOf(false)
    var openChat by mutableStateOf( Chat())
    private var mediaPlayer = MediaPlayer()
    var isErrorDialogShown by mutableStateOf(false)
    var lazyColumnChangeTrigger by mutableStateOf(0)

    override fun onSendClick(navController: NavController?){
        // create new user message and add it to the chat
        if (messageTextInput.text.trim().isNotBlank()){
            val newMessage = Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = messageTextInput.text.trim(), role = "user", createdAt = "")
            openChat.messages.add(newMessage)
            scrollPosition = openChat.messages.size -1
            restMessageTextFieldToDefault()
            isTextFieldCardShown = false
            getChatGbtResponse()
            saveMessageToDB(newMessage)

        }
    }

     private fun getChatGbtResponse() {
         val list = arrayListOf<ChatGBTMessage>()
         openChat.messages.forEach { msg ->
             list.add(ChatGBTMessage(role = msg.role, content = msg.content))
         }

         ///////////////////////////// simulation //////////////////

         viewModelScope.launch {


             val tempMessage = Message(id = UUID.randomUUID().toString(), chatId = openChat.id, content = "", role = "system", createdAt = "", isContentLoading = true)
             openChat.messages.add(tempMessage)
             lazyColumnChangeTrigger =+ 1
            var chatRes: ChatCompletionRes
            repository?.sentChatRequest(list)?.onEach {
                when(it){
                    is DataState.Success ->{
                        chatRes = it.data
                        val resMessage = chatRes.choices[0].message.content

                        displayChatResponse(resMessage, tempMessage.id)

                        saveMessageToDB(tempMessage)
                    }
                    is DataState.Error -> {
                        isErrorDialogShown = true
                        openChat.messages.remove(tempMessage)
                        lazyColumnChangeTrigger += 1
                    }
                    DataState.Loading -> {

                         changeMessageLoadingStatus(true, tempMessage.id)
                    }
                }
            }?.launchIn(viewModelScope)
        }
    }

    fun setCurrentOpenChat(id:String) {
        val res = chats.filter { it.id == id }
        if (res.isNotEmpty()){
            openChat = res[0]
            scrollPosition = openChat.messages.size -1
            if (openChat.messages[openChat.messages.size -1].role == "user"){ // check if this is the last message was from the user then get chatgbt response
                getChatGbtResponse()
            }
        }else{
            Toast.makeText(context, "\"no chat was this id was found\"B", Toast.LENGTH_SHORT).show()
  
        }

    }
    private fun changeMessageLoadingStatus(isLoading:Boolean, messageId:String){
        openChat.messages.forEach { msg ->
            if (msg.id == messageId){
                msg.isContentLoading = isLoading
            }
        }
        lazyColumnChangeTrigger += 1
    }
    fun onBackClick(navController: NavController?){
        if (isTextFieldCardShown){
            isTextFieldCardShown = false
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
        openChat.messages.forEach { msg ->
            msg.readingState = MessageReadingState.READY.value
            if (msg.isContentLoading){
                openChat.messages.remove(msg)
            }
        }
    }


    private fun saveMessageToDB(){
//        viewModelScope.launch {
//            repository?.saveMessages(newMessage)?.onEach {
//
//            }?.launchIn(viewModelScope)
//        }
    }
    fun readMessage(text:String, messageId:String) {
        viewModelScope.launch {
            repository?.generateSpeechFromText(text)?.onEach { res->
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
            }?.launchIn(viewModelScope)
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

    fun stopAudio(messageId: String = "" ){
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

    fun pauseAudio(messageId: String = ""){
        try {
            mediaPlayer.pause()
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
            openChat.messages.forEach { msg ->
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
                lazyColumnChangeTrigger += 1
            }
        }

    }
    private fun updateMessageReadingState(state:String, messageId:String){
        openChat.messages.forEach { msg ->
            if (msg.id == messageId){
                msg.readingState = state
            }
        }
        chatGbtLoading = false
        openChat.messages = openChat.messages
        lazyColumnChangeTrigger += 1
    }


}