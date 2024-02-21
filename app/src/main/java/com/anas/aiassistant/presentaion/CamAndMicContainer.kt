package com.anas.aiassistant.presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.R


@Composable
fun CamAndMicContainer() {
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
                .padding(start = 13.dp, end = 13.dp)
        ){
//            Image(
//                painter = painterResource(id = R.drawable.mic_icon) ,
//                contentDescription ="Microphone",
//                modifier = Modifier
//                    .width(23.dp)
//                    .height(23.dp))
//            val micIcon = ImageVector.vectorResource(id = R.drawable.mic_icon)
            CircularIconButton(onClick = {}, icon = painterResource(id = R.drawable.mic_icon), contentDescription = "mic" )
            CircularIconButton(onClick = {}, icon = painterResource(id = R.drawable.camera_icon), contentDescription = "mic" )
//            Image(
//                painter = painterResource(id = R.drawable.camera_icon) ,
//                contentDescription ="Camera",
//                modifier = Modifier
//                    .width(23.dp)
//                    .height(23.dp))
        }
    }

}


@Preview
@Composable
fun CamAndMicContainerPrev() {
    CamAndMicContainer()
}