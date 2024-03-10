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
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.dataState.MessageTextFieldState
import com.anas.aiassistant.model.ChatForDB
import com.anas.aiassistant.model.DatabaseRepository
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.model.RemoteRepository
import com.anas.aiassistant.shared.BaseViewmodelEvents
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.AppMainColor
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModel(
    private val remoteRepository: RemoteRepository,
    private val context: Application,
    private val databaseRepository: DatabaseRepository,
    private val speechRecognizer :SpeechRecognizer
):ViewModel() {


    var chatList:List<ChatForDB> by mutableStateOf(arrayListOf())
    val textFieldState = MutableStateFlow(MessageTextFieldState())
    abstract fun onSendClick(navController: NavController?)
    fun onEvent(event: BaseViewmodelEvents) {
        when (event) {
            BaseViewmodelEvents.OnKeyboardIconClick -> onKeyboardIconClick()
            BaseViewmodelEvents.OnMicClick -> {
                handelMickClick()
            }
            is BaseViewmodelEvents.OnSendClick -> onSendClick(event.navController)
            is BaseViewmodelEvents.SetMessageInput -> {
                onValueChange(event.text)
            }
        }
    }

    private fun onValueChange(text:String, ) {
//       cursorPosition = text.length
//        messageTextInput = TextFieldValue(text, TextRange(cursorPosition))
        textFieldState.update { it.copy(
            messageTextInput = TextFieldValue(text, TextRange(text.length)),
            cursorPosition = text.length,
            sendIconColor = if (text.trim().isBlank()) SendIconNotClickableColor else SendIconClickableColor
        ) }

//        sendIconColor = if (text.trim().isBlank()) {
//            SendIconNotClickableColor
//        } else {
//            SendIconClickableColor
//        }

    }
    private fun handelMickClick() {

        textFieldState.update { it.copy(
            isTextFieldCardShown = true, // expend the text field card in chat screen
            messageTextFieldFocus = false
        ) }

//       isTextFieldCardShown = true // expend the text field card in chat screen
//        messageTextFieldFocus = false
        if (textFieldState.value.mediaContainerWidth == 180.dp){ // when stopped listening
            this.speechRecognizer.destroy()
            restMediaContainerToDefault()
        }else{ // when listening
            viewModelScope.launch { // extend the media container with animation
                for (i in 1..5) {
                    delay(50)
                    textFieldState.update { it.copy(
                        mediaContainerWidth = it.mediaContainerWidth + 10.dp
                    ) }

                }
            }
            textFieldState.update { it.copy(
                micBackground = Color.Gray,
                messageTextInputHint = "Listening ...",
                bottomCardKeyBoardIconVisible = true
            ) }
            startListening(this.speechRecognizer)
//            bottomCardKeyBoardIconVisible = true
        }

        textFieldState.update { it.copy(
            launchedEffectTFFocusTrigger = it.launchedEffectTFFocusTrigger + 1
        ) }

    }


   protected fun saveMessageToDB(message: Message){
       viewModelScope.launch {
           databaseRepository.saveMessage(message).onEach {
               when(it){
                   is DataState.Error -> {
                       Toast.makeText(context, "Error saving message to local DB", Toast.LENGTH_SHORT).show()
                   }
                   DataState.Loading -> {

                   }
                   is DataState.Success -> {

                   }
               }
           }.launchIn(viewModelScope)
       }
   }


    protected fun saveChatToDB(chat:ChatForDB){
        viewModelScope.launch {
            databaseRepository.saveChatToDB(chat)
        }
    }
    private fun restMediaContainerToDefault(){
      viewModelScope.launch {

          if (textFieldState.value.mediaContainerWidth > 150.dp){
              for (i in 1..5) {
                  delay(50)
                  textFieldState.update { it.copy(
                      mediaContainerWidth = it.mediaContainerWidth - 10.dp
                  ) }
              }
          }

        }

        textFieldState.update { it.copy(
            micBackground = AppMainColor,
            messageTextInputHint = text_field_hint_expanded,
            bottomCardKeyBoardIconVisible = false
        ) }
//        micBackground = AppMainColor
//        messageTextInputHint = text_field_hint_expanded
//        bottomCardKeyBoardIconVisible = false
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
                    results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.get(0)?.let { resText ->
                        if (textFieldState.value.messageTextInput.text.isBlank()){
                            textFieldState.update { it.copy(
                                messageTextInput = TextFieldValue(resText, TextRange(resText.length))
                            ) }
//                            messageTextInput = TextFieldValue(it, TextRange(it.length))
                        }else{ // add to the existing text
                            textFieldState.update { it.copy(
                                cursorPosition = it.messageTextInput.text.length + resText.length + 1, /// add one for the space
                                messageTextInput = TextFieldValue("${it.messageTextInput.text.trim()} $resText", TextRange(it.messageTextInput.text.length + resText.length + 1))
                            ) }
//                            cursorPosition = messageTextInput.text.length + it.length + 1 /// add one for the space
//                            messageTextInput = TextFieldValue("${messageTextInput.text.trim()} $it", TextRange(cursorPosition))

                        }
                        textFieldState.update { it.copy(
                            sendIconColor = if (resText.isNotBlank()) SendIconClickableColor else  SendIconClickableColor
                        ) }

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

        textFieldState.update { it.copy(
            messageTextInput = TextFieldValue("", TextRange(0)),
            messageTextFieldFocus = false,
            sendIconColor = SendIconNotClickableColor
        ) }
//        messageTextInput = TextFieldValue("", TextRange(0))
        restMediaContainerToDefault()
//        messageTextFieldFocus = false
//        sendIconColor = SendIconNotClickableColor
    }
    private fun onKeyboardIconClick(){
        restMediaContainerToDefault()
        speechRecognizer.destroy()
        textFieldState.update { it.copy(
            messageTextFieldFocus = true,
        ) }

    }

}