package com.anas.aiassistant.presentaion.chat

import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel
import com.anas.aiassistant.presentaion.ErrorDialog
import com.anas.aiassistant.presentaion.LoadingSpinner
import com.anas.aiassistant.presentaion.ScreenTopBar
import com.anas.aiassistant.presentaion.bottomCard.BottomCard
import com.anas.aiassistant.presentaion.chat.message.MessageRow
import com.anas.aiassistant.shared.ChatScreenEvent


@Composable
fun ChatScreen(navController: NavController?) {
    val viewModel = hiltViewModel<ChatScreenViewModel>()
    val listState = rememberLazyListState()
    val backStackEntry by navController?.currentBackStackEntryAsState()!!
    val chatId:String? = backStackEntry?.arguments?.getString("chatId")
    val textFieldState by viewModel.textFieldState.collectAsState()
    val openChat by viewModel.openChat.collectAsState()
    if (chatId != null){
        LaunchedEffect(chatId) {
            viewModel.setCurrentOpenChat(chatId)
        }

    }

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackClick(navController)
            }
        }
    }
    BackHandler(onBack = { backCallback.handleOnBackPressed() })
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {


        ScreenTopBar(openChat.title, onBackIconClick = {
            viewModel.onScreenEvent(ChatScreenEvent.OnArrowBackClick(navController!!))
        })

        LazyColumn(state = listState, modifier = Modifier.weight(1f)){
            items(openChat.messages){ message ->
                MessageRow(message, viewModel::onScreenEvent)
            }
        }


        //// this text is not visible and its only purpose to trigger the app recompose the lazy column when any change is done to the message list
               Text(text = "${viewModel.lazyColumnChangeTrigger}", modifier = Modifier.size(0.dp) )
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        if (textFieldState.isTextFieldCardShown){
            BottomCard(navController = navController, viewModel::onEvent, textFieldState)
//            viewModel.messageTextFieldFocus = true
        }else{
            BottomChatCard(textFieldState, viewModel::onEvent, viewModel::onScreenEvent)
        }

        // Restore the scroll position when the composable is first displayed
        LaunchedEffect(viewModel.scrollPositionTrigger, viewModel.scrollPosition) {

            try {
                if (openChat.messages.size> 1 && !viewModel.isErrorDialogShown){
                    listState.scrollToItem(openChat.messages.size - 1)
                }
            }catch (e: IndexOutOfBoundsException){
              Log.e("message list scroll", e.message, e)
            }
        }
    }

    //  // display an error dialog when needed
    ErrorDialog(onOkClick = { viewModel.isErrorDialogShown = !viewModel.isErrorDialogShown}, isVisible = viewModel.isErrorDialogShown )
    // display a loading spinner
    LoadingSpinner(isVisible = textFieldState.chatGbtLoading)

}



@Preview(device = "id:pixel_6_pro")
@Composable
fun ChatScreenPreview(){
//     ChatScreen(null, Main ViewModel())
}