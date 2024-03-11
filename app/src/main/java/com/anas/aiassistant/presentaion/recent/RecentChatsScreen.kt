package com.anas.aiassistant.presentaion.recent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.anas.aiassistant.data.AppData.chats
import com.anas.aiassistant.domain.viewModel.RecentChatsViewModel
import com.anas.aiassistant.presentaion.ScreenTopBar
import com.anas.aiassistant.shared.RecentChatsScreenEvent
import java.time.ZonedDateTime

@Composable
fun RecentChatsScreen(navController: NavHostController?) {

    val viewModel = hiltViewModel<RecentChatsViewModel>()

    LaunchedEffect(Unit){
        viewModel.chatList = ArrayList(chats)
        viewModel.init()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        ScreenTopBar(
            title = "Chats",
            onBackIconClick = { viewModel.onEvent(RecentChatsScreenEvent.OnBackClick(navController!!)) }
        )

        LazyColumn{
            itemsIndexed(viewModel.chatList){index, chat ->
                var label = ""

                label = viewModel.getLabel(ZonedDateTime.parse(chat.lastMessageTimestamp))
                var isLabelVisible = true

                if (index != 0 ){
                    val previousChatLabel = viewModel.getLabel(ZonedDateTime.parse(viewModel.chatList[index -1].lastMessageTimestamp))
                    isLabelVisible = previousChatLabel != label
                }
                DetailRecentRow(
                      label = label,
                      isLabelVisible  = isLabelVisible,
                      messageTitle = chat.title,
                      chatId = chat.id,
                      onItemClick = {
                          viewModel.onEvent(RecentChatsScreenEvent.OnItemClick(chat.id, navController!!))
                      },
                     onEvent = viewModel::onEvent
                    )

            }
        }
    }
}

@Preview(device = "id:pixel_6_pro")
@Composable
fun RecentChatsScreenPreview() {
    RecentChatsScreen(null)
}