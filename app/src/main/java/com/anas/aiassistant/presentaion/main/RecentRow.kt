package com.anas.aiassistant.presentaion.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anas.aiassistant.R

@Composable
fun RecentRow(chatTitle:String, chatId:String, navController:NavController?){
    val image: Painter = painterResource(id = R.drawable.history_recent_icon)
    Row (
        modifier = Modifier
            .background(Color.White)
            .padding(10.dp)
            .fillMaxWidth()
            .clickable { onRecentItemClick(chatId, navController!!) },
        verticalAlignment = Alignment.CenterVertically)
    {
       Card (modifier = Modifier
           .width(32.dp)
           .height(32.dp)
           ) {
           Image(painter = image , contentDescription = "recentIcon", modifier = Modifier.padding(7.dp))
       }
       Text(text = chatTitle, modifier = Modifier.padding(start = 10.dp), fontSize = 17.sp)
   }

}

fun onRecentItemClick(chatId:String, navController: NavController){
    navController.navigate("chat_screen/${chatId}")
}

@Preview
@Composable
fun RecentRowPreview(){
    RecentRow("hello there", "", null)
}