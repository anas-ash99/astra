package com.anas.aiassistant.presentaion.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.R
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel
import com.anas.aiassistant.shared.StringValues.text_field_hint_short
import com.anas.aiassistant.ui.theme.AppMainColor

@Composable
fun BottomChatCard(mainViewModel: ChatScreenViewModel?) {
    Column(
        modifier = Modifier
            .background(Color.White),
    ) {
        Divider(color = Color.LightGray, thickness = 1.dp,   )
        Surface(
            modifier = Modifier
                .height(70.dp)
                .padding(3.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, Color(0xFFdce2e9)),
        ) {

            Row(
                horizontalArrangement= Arrangement.SpaceBetween,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().clickable {
                    mainViewModel?.isTextFieldCardShown = true
                    mainViewModel?.messageTextInput = ""
                    mainViewModel?.messageTextFieldFocus = true
                }

            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp).fillMaxWidth(0.5f),
                    text = text_field_hint_short,
                    fontSize = 15.sp,
                    color = mainViewModel?.messageTexFieldColor!!
                )
                IconsContainer()
            }


        }

    }
}

@Composable
fun IconsContainer(){
    Card(
        modifier = Modifier
            .height(55.dp)
            .width(130.dp)
            .padding(end = 5.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppMainColor // Apply your custom color here
        ),
        shape = RoundedCornerShape(28.dp),
    ){
        Row (
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment= Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 25.dp, end = 25.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.mic_icon) ,
                contentDescription ="Microphone",
                modifier = Modifier
                    .width(23.dp)
                    .height(23.dp))
            Image(
                painter = painterResource(id = R.drawable.camera_icon) ,
                contentDescription ="Camera",
                modifier = Modifier
                    .width(23.dp)
                    .height(23.dp))
        }
    }
}

@Preview
@Composable
fun BottomChatCardPreview() {
    BottomChatCard(ChatScreenViewModel())
}