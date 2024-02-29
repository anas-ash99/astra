package com.anas.aiassistant.presentaion.bottomCard

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.anas.aiassistant.domain.viewModel.BaseViewModel
import com.anas.aiassistant.presentaion.CircularIconButton
import com.anas.aiassistant.shared.BaseViewmodelEvents

@Composable
fun BottomCard(viewModel: BaseViewModel?, navController: NavController?) {

    Surface(
        modifier = Modifier
            .height(210.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        border = BorderStroke(1.dp, Color(0xFFdce2e9)),
        shadowElevation = 10.dp,
        color = Color.White
    ) {

        Column(modifier = Modifier.padding()) {

            CustomTextField(viewModel)
            Row(
                horizontalArrangement= Arrangement.SpaceBetween,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()

            ) {

                Card(modifier = Modifier // add this card to make space at the start of the row
                    .height(0.dp)
                    .width(0.dp)) {
                }

                CamAndMicContainer(viewModel = viewModel!!)
                val sendIconPainter = rememberVectorPainter(image = Icons.AutoMirrored.Filled.Send)
                CircularIconButton(
                    onClick = {
                        viewModel.onEvent(BaseViewmodelEvents.OnSendClick(navController))

                },
                    icon = sendIconPainter,
                    contentDescription = "mic",
                    iconTint = viewModel.sendIconColor,
                    backgroundColor = Color.White
                )

            }

        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun BottomCardPreview() {
//    BottomCard(MainViewModel(), null)
}




