package com.anas.aiassistant.presentaion.recent

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun RecentChatsScreen(navController: NavHostController?) {
       Text(text = "Recent chats screen", modifier = Modifier.padding(50.dp))
}


@Preview
@Composable
fun RecentChatsScreenPreview() {
    RecentChatsScreen(null)
}