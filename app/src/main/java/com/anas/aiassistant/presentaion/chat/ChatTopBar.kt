package com.anas.aiassistant.presentaion.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel
import com.anas.aiassistant.presentaion.CircularIconButton


@Composable
fun ChatTopBar(navController: NavController?,viewModel: ChatScreenViewModel){
    val backIconPainter = rememberVectorPainter(image = Icons.AutoMirrored.Filled.KeyboardArrowLeft)
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .height(55.dp)
            .background(Color.White)
            .padding(start = 5.dp, end = 5.dp,),
        horizontalArrangement= Arrangement.SpaceBetween,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        CircularIconButton(
            iconSize = (40.dp),
            onClick = {
                viewModel.onBackClick(navController)
            },
            icon = backIconPainter ,
            contentDescription ="Go Back Icon",
            backgroundColor = Color.White
        )

        Text(
            text = viewModel.openChat.title,
            fontSize = 19.sp,
            fontWeight = FontWeight.Normal,
        )

        Image(
            imageVector = Icons.Filled.AccountCircle,

            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(20.dp)) // Clip with rounded corners (50% radius)
        )
    }


}



@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun prev() {
//    ChatTopBar(navController =null , viewModel = ChatScreenViewModel() )
}