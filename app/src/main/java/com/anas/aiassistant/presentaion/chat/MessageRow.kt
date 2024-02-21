package com.anas.aiassistant.presentaion.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel

@Composable
fun MessageRow(role:String, content:String, viewModel: ChatScreenViewModel){
    Column(
        modifier = Modifier.fillMaxWidth()) {
        if (role == "user"){
            Surface (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 10.dp, top = 10.dp, bottom = 10.dp),
                shape = RoundedCornerShape(topEnd = 0.dp, topStart = 25.dp, bottomStart = 25.dp, bottomEnd = 25.dp),
                color = Color(0xFFF0F3F8),
            ){
                Text(
                    text = content,
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 15.dp, end = 10.dp, top = 15.dp),
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            }

        }else{
            MessageHeader(content, viewModel)
            Text(
                text = content,
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 15.dp, end = 10.dp, top = 10.dp),
                fontSize = 16.sp,
                lineHeight = 22.sp
            )

        }
    }
}