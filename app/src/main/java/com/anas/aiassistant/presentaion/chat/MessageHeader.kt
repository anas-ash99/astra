package com.anas.aiassistant.presentaion.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.R
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel

@Composable
fun MessageHeader(msg:String, viewModel: ChatScreenViewModel){

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 20.dp, top = 15.dp),
        horizontalArrangement= Arrangement.SpaceBetween,
        verticalAlignment= Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ai_app_icon),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(15.dp))
        )
        Image(
            painter = painterResource(id = R.drawable.speaker_icon),
            contentDescription = "Read Message",
            modifier = Modifier
                .height(22.dp)
                .width(22.dp)
                .clickable {
                    viewModel.readMessage(msg)
                }
        )

    }
}