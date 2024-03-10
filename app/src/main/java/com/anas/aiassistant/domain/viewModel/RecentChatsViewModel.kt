package com.anas.aiassistant.domain.viewModel

import android.app.Application
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anas.aiassistant.data.AppData
import com.anas.aiassistant.dataState.DataState
import com.anas.aiassistant.model.ChatForDB
import com.anas.aiassistant.model.DatabaseRepository
import com.anas.aiassistant.shared.RecentChatsScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecentChatsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val context: Application
)  :ViewModel() {



    var isInitialized by mutableStateOf(false)
    val chatList = mutableStateOf(AppData.chats2)
    fun onEvent(event:RecentChatsScreenEvent){
        when(event){
            is RecentChatsScreenEvent.OnDeleteItemClick -> {
                Toast.makeText(context, "delete item ${event.id.take(5)}", Toast.LENGTH_SHORT).show()
            }
            is RecentChatsScreenEvent.OnItemClick -> {
                event.navController.navigate("chat_screen/${event.id}")
            }
            is RecentChatsScreenEvent.OnPinItemClick -> {
                Toast.makeText(context, "Pin item ${event.id.take(5)}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteChat(id:String){

        viewModelScope.launch {
            val res = databaseRepository.deleteChatById(id)
            if (!res){
                Toast.makeText(context, "error deleting the chat", Toast.LENGTH_SHORT).show()
            }else{
                AppData.chats2.removeIf { it.id == id }
            }
        }
    }

    private fun updateChat(chat: ChatForDB){

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
//                        chatList = it.data
                    }
                }
            }.launchIn(viewModelScope)
        }
    }


}