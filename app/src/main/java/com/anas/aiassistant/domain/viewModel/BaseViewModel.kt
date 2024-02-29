package com.anas.aiassistant.domain.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.anas.aiassistant.data.AppData
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.Repository
import com.anas.aiassistant.shared.BaseViewmodelEvents
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.AppMainColor
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModel(
    private val repository: Repository,
    private val context: Application
):ViewModel() {

    private var speechRecognizer :SpeechRecognizer? = null
    var chatList by mutableStateOf(AppData.chats)
    var sendIconColor by mutableStateOf(SendIconNotClickableColor)
    var chatGbtLoading by mutableStateOf(false)
    var messageTextInput by mutableStateOf(TextFieldValue(""))
    var messageTextInputHint by mutableStateOf(text_field_hint_expanded)
    var messageTextFieldFocus by mutableStateOf(false)
    var mediaContainerWidth by mutableStateOf(130.dp)
    var bottomCardKeyBoardIconVisible by  mutableStateOf(false)
    var launchedEffectTFFocusTrigger by  mutableStateOf(0)
    var micBackground by  mutableStateOf(AppMainColor)
    var cursorPosition by mutableStateOf(0) // Initial cursor position
    abstract fun onSendClick(navController: NavController?)
    fun onEvent(event: BaseViewmodelEvents) {
        when (event) {
            BaseViewmodelEvents.OnKeyboardIconClick -> onKeyboardIconClick()
            is BaseViewmodelEvents.OnMicClick -> {
                onMickClick(event.speechRecognizer)
            }
            is BaseViewmodelEvents.OnSendClick -> onSendClick(event.navController)
            is BaseViewmodelEvents.SetMessageInput -> {
                onValueChange(event.text)
            }
        }
    }

    private fun onValueChange(text:String, ) {
       cursorPosition = text.length
        messageTextInput = TextFieldValue(text, TextRange(cursorPosition))
        sendIconColor = if (text.trim().isBlank()) {
            SendIconNotClickableColor
        } else {
            SendIconClickableColor
        }
    }
    fun onMickClick(speechRecognizer: SpeechRecognizer) {
        if(this.speechRecognizer == null){
            this.speechRecognizer = speechRecognizer
        }
        messageTextFieldFocus = false
        if (mediaContainerWidth == 180.dp){ // when stopped listening
            this.speechRecognizer?.destroy()
            restMediaContainerToDefault()
        }else{ // when listening

            viewModelScope.launch { // extend the media container with animation

                for (i in 1..5) {
                    delay(50)
                    mediaContainerWidth += 10.dp
                }
            }
//            mediaContainerWidth = 180.dp
            micBackground = Color.Gray
            messageTextInputHint = "Listening ..."
            startListening(this.speechRecognizer!!)
            bottomCardKeyBoardIconVisible = true
        }

        launchedEffectTFFocusTrigger += 1


    }


   protected fun saveMessageToDB(message:Message){
       viewModelScope.launch {
           repository?.saveMessages(message)?.onEach {
               when(it){
                   is DataState.Error -> {
                       Toast.makeText(context, "Error saving message to local DB", Toast.LENGTH_SHORT).show()
                   }
                   DataState.Loading -> {

                   }
                   is DataState.Success -> {

                   }
               }
           }?.launchIn(viewModelScope)
       }
   }
    private fun restMediaContainerToDefault(){
      viewModelScope.launch {

          if (mediaContainerWidth > 150.dp){
              for (i in 1..5) {
                  delay(50)
                  mediaContainerWidth -= 10.dp
              }
          }

        }

        micBackground = AppMainColor
        messageTextInputHint = text_field_hint_expanded
        bottomCardKeyBoardIconVisible = false
    }
    fun onMediaUploadClick(){

    }

    private fun startListening(speechRecognizer: SpeechRecognizer) {
        speechRecognizer.apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) = Unit
                override fun onBeginningOfSpeech() = Unit
                override fun onRmsChanged(rmsdB: Float) = Unit
                override fun onBufferReceived(buffer: ByteArray?) = Unit
                override fun onEndOfSpeech() = Unit
                override fun onError(error: Int) {
                    Log.e("audio listening", error.toString())
                    restMediaContainerToDefault()
                    Toast.makeText(context, "Error starting the speech recognizer", Toast.LENGTH_SHORT).show()
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
                override fun onPartialResults(partialResults: Bundle?) =Unit


                override fun onResults(results: Bundle?) {
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)?.let {
                        if (messageTextInput.text.isBlank()){

                            messageTextInput = TextFieldValue(it, TextRange(it.length))
                        }else{ // add to the existing text
                            cursorPosition = messageTextInput.text.length + it.length + 1 /// add one for the space
                            messageTextInput = TextFieldValue("${messageTextInput.text.trim()} $it", TextRange(cursorPosition))

                        }
                        if (it.isNotBlank())
                            sendIconColor = SendIconClickableColor
                        restMediaContainerToDefault()
                    }
                }

            })

            startListening(Intent().apply {
                action = RecognizerIntent.ACTION_RECOGNIZE_SPEECH
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            })
        }
    }
    fun restMessageTextFieldToDefault(){
        messageTextInput = TextFieldValue("", TextRange(0))
        restMediaContainerToDefault()
        messageTextFieldFocus = false
        sendIconColor = SendIconNotClickableColor
    }
    private fun onKeyboardIconClick(){
        restMediaContainerToDefault()
        speechRecognizer?.destroy()
        messageTextFieldFocus = true

    }

}