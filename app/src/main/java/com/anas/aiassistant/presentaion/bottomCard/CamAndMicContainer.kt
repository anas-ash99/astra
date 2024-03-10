package com.anas.aiassistant.presentaion.bottomCard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.R
import com.anas.aiassistant.dataState.MessageTextFieldState
import com.anas.aiassistant.presentaion.CircularIconButton
import com.anas.aiassistant.shared.BaseViewmodelEvents


@Composable
fun CamAndMicContainer(modifier: Modifier = Modifier, state:MessageTextFieldState, onEvent:(BaseViewmodelEvents)->Unit) {

    val keyboardIconPainter = rememberVectorPainter(image = Icons.Outlined.KeyboardAlt)

    Card(
        modifier = modifier
            .height(55.dp)
            .width(state.mediaContainerWidth),
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
                .padding(start = 13.dp, end = 13.dp)
        ){


            if (state.bottomCardKeyBoardIconVisible){
                CircularIconButton(
                    onClick = {onEvent(BaseViewmodelEvents.OnKeyboardIconClick)},
                    icon = keyboardIconPainter, contentDescription = "keyboard" )
            }
            CircularIconButton(
                onClick = { onEvent(BaseViewmodelEvents.OnMicClick) },
                icon = painterResource(id = R.drawable.mic_icon),
                contentDescription = "mic",
                backgroundColor = state.micBackground)
            CircularIconButton(onClick = {}, icon = painterResource(id = R.drawable.camera_icon), contentDescription = "camera" )

        }
    }

}




@Preview
@Composable
fun CamAndMicContainerPrev() {
//    CamAndMicContainer(viewModel = MainViewModel())
}