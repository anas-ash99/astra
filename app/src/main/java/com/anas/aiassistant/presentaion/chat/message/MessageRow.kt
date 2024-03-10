package com.anas.aiassistant.presentaion.chat.message

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.model.Message
import com.anas.aiassistant.shared.ChatScreenEvent

@Composable
fun MessageRow(message: Message, onEvent:(ChatScreenEvent) -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth().background(Color.White)) {
        if (message.role == "user"){
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                shape = RoundedCornerShape(topEnd = 0.dp, topStart = 25.dp, bottomStart = 25.dp, bottomEnd = 25.dp),
                color = Color(0xFFF0F3F8),
            ){

                Text(
                    text = message.content,
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 15.dp, end = 10.dp, top = 15.dp),
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }

        }else{
            MessageHeader(message, onEvent)

            if (message.isContentLoading){
                Box (
                    modifier = Modifier
                        .padding(start = 15.dp, top = 10.dp)
                        ,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp),
                        color = Color.DarkGray,
                    )
                }

            }else{
                Text(
                    text = message.content,
                    modifier = Modifier
                        .padding(start = 10.dp, bottom = 15.dp, end = 10.dp, top = 10.dp),
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }

        }
    }
}


@Preview
@Composable
fun MessageRowPreview() {
//    MessageRow(Message("","", "Hi there", "System", "",  readingState = MessageReadingState.READING.value), ChatScreenViewModel())
}