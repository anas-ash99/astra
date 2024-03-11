package com.anas.aiassistant.presentaion.bottomCard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.getValue
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
    val animatedSize by animateDpAsState(
        targetValue = if (state.isIconsContainerExpanded) 180.dp else 130.dp,
        animationSpec = tween(durationMillis = 800), label = "" // Customize duration
    )

    val animatedOpacity by animateFloatAsState(
        targetValue = if (state.isIconsContainerExpanded) 1f else 0f,
        animationSpec = tween(durationMillis = 800), label = "" // Customize duration
    )
    Card(
        modifier = modifier
            .height(55.dp)
            .width(animatedSize),
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


            CircularIconButton(onClick = {}, icon = painterResource(id = R.drawable.camera_icon), contentDescription = "camera" )
            CircularIconButton(
                onClick = { onEvent(BaseViewmodelEvents.OnMicClick) },
                icon = painterResource(id = R.drawable.mic_icon),
                contentDescription = "mic",
                backgroundColor = state.micBackground
            )

            AnimatedVisibility(
                visible = state.isIconsContainerExpanded,
                enter = fadeIn(animationSpec = tween(800)),
                exit = fadeOut(animationSpec = tween(800))
            ) {
                CircularIconButton(
                    onClick = {
                        onEvent(BaseViewmodelEvents.OnKeyboardIconClick)
                    },
                    icon = keyboardIconPainter,
                    contentDescription = "keyboard",
                )
            }

        }
    }

}


@Preview
@Composable
fun CamAndMicContainerPrev() {
//    CamAndMicContainer(viewModel = MainViewModel())
}