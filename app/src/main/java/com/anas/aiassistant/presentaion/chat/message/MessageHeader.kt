package com.anas.aiassistant.presentaion.chat.message

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.R
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.shared.MessageReadingState

@Composable
fun MessageHeader(message: Message, viewModel: ChatScreenViewModel){

    Row(
        modifier= Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 20.dp, top = 15.dp)
            .background(Color.White),
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

        when (message.readingState) {
            MessageReadingState.LOADING.value -> {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp),
                    color = Color.DarkGray,
                )
            }
            MessageReadingState.READY.value -> {

                Image(
                    painter = painterResource(id = R.drawable.speaker_icon),
                    contentDescription = "Read Message",
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                        .clickable {
                            viewModel.readMessage(message.content, message.id)
                        }
                )
            }
            MessageReadingState.READING.value -> {
                Icon(
                    imageVector = Icons.Filled.Pause,
                    contentDescription = "Pause Message",
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                        .clickable {
                            viewModel.stopAudio(message.id)
                        }
                )
            }
        }

    }
}

@Preview
@Composable
fun MessageHeaderPreview() {
//    MessageHeader(Message("", "","", "", ""), viewModel = ChatScreenViewModel())
}