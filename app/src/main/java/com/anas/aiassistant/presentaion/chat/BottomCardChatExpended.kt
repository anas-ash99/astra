package com.anas.aiassistant.presentaion.chat

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.domain.viewModel.ChatScreenViewModel
import com.anas.aiassistant.presentaion.CamAndMicContainer
import com.anas.aiassistant.presentaion.CircularIconButton
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.SendIconClickableColor
import com.anas.aiassistant.ui.theme.SendIconNotClickableColor
import com.anas.aiassistant.ui.theme.TextPrimaryColor



@Composable
fun BottomCardChatExpended(viewModel: ChatScreenViewModel?) {

    Surface(
        modifier = Modifier
            .height(210.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        border = BorderStroke(1.dp, Color(0xFFdce2e9)),
        shadowElevation = 10.dp
    ) {

        Column(modifier = Modifier.padding(end=16.dp, start = 16.dp, top = 15.dp)) {


            ClickableTextFieldChat(viewModel)

            Row(
                horizontalArrangement= Arrangement.SpaceBetween,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()


            ) {

                Card(modifier = Modifier // add this card to make space at the start of the row
                    .height(0.dp)
                    .width(0.dp)) {
                }

                CamAndMicContainer()
                val sendIconPainter = rememberVectorPainter(image = Icons.Filled.Send)
                CircularIconButton(onClick = {
                    viewModel?.onSendClick()
                },
                    icon = sendIconPainter,
                    contentDescription = "mic",
                    iconTint = viewModel?.sendIconColor!!,
                    backgroundColor = Color.White
                )

            }
        }
    }
}


@Composable
fun ClickableTextFieldChat(mainViewModel: ChatScreenViewModel?) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(
        value = mainViewModel?.messageTextInput!!,
        onValueChange = {
            mainViewModel.messageTextInput = it
            if (it.trim().isBlank()){
                mainViewModel.sendIconColor = SendIconNotClickableColor
                mainViewModel.messageTexFieldColor = TextPrimaryColor
            }else{
                mainViewModel.sendIconColor = SendIconClickableColor
            }
                 },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        cursorBrush = SolidColor(Color.Black),
        textStyle = TextStyle(fontSize = 23.sp, color = mainViewModel.messageTexFieldColor),
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.68f)
            .focusRequester(focusRequester)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && mainViewModel.messageTextInput == text_field_hint_expanded ) {
                    mainViewModel.messageTextInput = ""
                    mainViewModel.messageTexFieldColor = TextPrimaryColor
                }
            }
        )
    LaunchedEffect(Unit) {
        if (mainViewModel.messageTextFieldFocus){
            focusRequester.requestFocus()
        }else{
            focusRequester.freeFocus()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun BottomCardPreview() {
    BottomCardChatExpended(ChatScreenViewModel())
}



