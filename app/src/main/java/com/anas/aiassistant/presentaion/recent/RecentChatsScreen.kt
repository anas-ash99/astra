package com.anas.aiassistant.presentaion.recent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.anas.aiassistant.data.AppData.chats2
import com.anas.aiassistant.domain.viewModel.RecentChatsViewModel
import com.anas.aiassistant.presentaion.ScreenTopBar
import com.anas.aiassistant.shared.RecentChatsScreenEvent
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun RecentChatsScreen(navController: NavHostController?) {


    val viewModel = hiltViewModel<RecentChatsViewModel>()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        ScreenTopBar(title = "Chats", onBackIconClick = {} )

        val sortedChats = chats2.sortedByDescending { it.createdAt }
        LazyColumn{
            itemsIndexed(sortedChats){index, chat ->
                val label = getLabel(ZonedDateTime.parse(chat.createdAt))
                var isLabelVisible = true

                if (index != 0 ){
                    val previousChatLabel = getLabel(ZonedDateTime.parse(sortedChats[index -1].createdAt))
                    isLabelVisible = previousChatLabel != label
                }

                DetailRecentRow(
                      label = label,
                      isLabelVisible  = isLabelVisible,
                      messageTitle = chat.title,
                      onItemClick = {
                          viewModel.onEvent(RecentChatsScreenEvent.OnItemClick(chat.id, navController!!))
                      },
                     onEvent = viewModel::onEvent
                    )

            }
        }
    }
}


fun getLabel(date: ZonedDateTime): String {
    val now = ZonedDateTime.now()
    return when {
        ChronoUnit.DAYS.between(date, now) == 0L -> "Today"
        ChronoUnit.DAYS.between(date, now) <= 30 -> "Last 30 days"
        date.year == now.year -> date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        else -> "${date.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${date.year}"
    }
}

@Preview(device = "id:pixel_6_pro")
@Composable
fun RecentChatsScreenPreview() {
    RecentChatsScreen(null)
}