package com.anas.aiassistant.presentaion.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel
import com.anas.aiassistant.domain.viewModel.MainViewModel
import com.anas.aiassistant.presentaion.LoadingSpinner

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChatScreen(navController: NavController?, mainViewModel: MainViewModel?) {
    val myScreenViewModel: ChatScreenViewModel = viewModel()
    val listState = rememberLazyListState()
    val backStackEntry by navController?.currentBackStackEntryAsState()!!
    val chatId:String? = backStackEntry?.arguments?.getString("chatId")
    if (chatId != null){
        myScreenViewModel.openChat = myScreenViewModel.getChatById(chatId)
    }
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ChatTopBar(navController, mainViewModel!!)
        LazyColumn(state = listState, modifier = Modifier.weight(1f)){
            items(myScreenViewModel.openChat.messages){ message ->
                MessageRow(role = message.role, content = message.content, viewModel = mainViewModel)
            }
        }
        if (myScreenViewModel.isTextFieldCardShown){
            BottomCardChatExpended( myScreenViewModel)
        }else{
            BottomChatCard(myScreenViewModel)
        }

        // Restore the scroll position when the composable is first displayed
        LaunchedEffect(Unit) {
            listState.scrollToItem(mainViewModel.scrollPosition)
        }
    }

   // display a loading spinner
   LoadingSpinner(isVisible = myScreenViewModel?.chatGbtLoading!!)

}



@RequiresApi(Build.VERSION_CODES.O)
@Preview(device = "id:pixel_6_pro")
@Composable
fun ChatScreenPreview(){
     ChatScreen(null, MainViewModel())
}