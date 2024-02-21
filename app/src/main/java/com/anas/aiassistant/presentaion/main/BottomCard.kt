package com.anas.aiassistant.presentaion.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.anas.aiassistant.R
import com.anas.aiassistant.domain.viewModel.MainViewModel
import com.anas.aiassistant.shared.StringValues.text_field_hint_expanded
import com.anas.aiassistant.ui.theme.TextPrimaryColor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomCard(viewModel: MainViewModel?, navController: NavController?, chatId:String = "") {

    Surface(
        modifier = Modifier
            .height(210.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        border = BorderStroke(1.dp, Color(0xFFdce2e9)),
        shadowElevation = 10.dp
    ) {

        Column(modifier = Modifier.padding(end=16.dp, start = 16.dp, top = 15.dp)) {


            ClickableTextField(viewModel)

            Row(
                horizontalArrangement= Arrangement.SpaceBetween,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()


            ) {

                Card(modifier = Modifier // add this card to make space at the start of the row
                    .height(0.dp)
                    .width(0.dp)) {
                }

                IconsContainer()
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Send Message",
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                        .clickable {
                            viewModel?.onSendClick(navController!!, chatId = chatId)
                            },
                    tint = viewModel?.sendIconColor!!
                )
            }

        }


    }
}


@Composable
fun IconsContainer(){
    Card(
        modifier = Modifier
            .height(55.dp)
            .width(130.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFd3e3fd)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ClickableTextField(mainViewModel: MainViewModel?) {
    val focusRequester = remember { FocusRequester() }
    BasicTextField(
        value = mainViewModel?.messageTextInput!!,
        onValueChange = {
            mainViewModel.messageTextInput = it
            if (it.trim().isBlank()){
                mainViewModel.sendIconColor = Color(0xB7747474)

            }else{
                mainViewModel.sendIconColor = Color(0xFF222222)
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
    BottomCard(MainViewModel(), null)
}




