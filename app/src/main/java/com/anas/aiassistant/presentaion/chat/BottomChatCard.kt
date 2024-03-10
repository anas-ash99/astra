package com.anas.aiassistant.presentaion.chat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.dataState.MessageTextFieldState
import com.anas.aiassistant.presentaion.bottomCard.CamAndMicContainer
import com.anas.aiassistant.shared.BaseViewmodelEvents
import com.anas.aiassistant.shared.ChatScreenEvent
import com.anas.aiassistant.shared.StringValues.text_field_hint_short
import com.anas.aiassistant.ui.theme.TextSecondaryColor

@Composable
fun BottomChatCard(state: MessageTextFieldState, onEvent:(BaseViewmodelEvents)->Unit, onScreenEvent: (ChatScreenEvent) ->Unit) {
    Column(
        modifier = Modifier
            .background(Color.White),
    ) {
        Divider(color = Color.LightGray, thickness = 1.dp, )
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
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onScreenEvent(ChatScreenEvent.OnBottomChatCardClick)
//                        mainViewModel?.isTextFieldCardShown = true
//                        mainViewModel?.messageTextInput = TextFieldValue("", TextRange(0))
//                        mainViewModel?.messageTextFieldFocus = true
//                        mainViewModel?.launchedEffectTFFocusTrigger =+ 1
                    }

            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth(0.5f),
                    text = text_field_hint_short,
                    fontSize = 15.sp,
                    color = TextSecondaryColor
                )
                CamAndMicContainer(modifier = Modifier.padding(end = 5.dp), state, onEvent )
            }


        }

    }
}

@Preview
@Composable
fun BottomChatCardPreview() {
//    BottomChatCard(ChatScreenViewModel())
}