package com.anas.aiassistant.presentaion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ErrorDialog (dialogMessage: String = "Something went wrong playing the audio", onOkClick:()->Unit, isVisible:Boolean){

     if (!isVisible) return
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .fillMaxHeight()
            .background(Color(0xB55F5F5F))
            .clickable { onOkClick() },

        contentAlignment = Alignment.Center
    ) {
      Surface(
          modifier = Modifier
              .fillMaxWidth()
              .padding(start = 15.dp, end = 15.dp)
              . height(180.dp),
          shape = RoundedCornerShape(10.dp)
      ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()

        ) {
            Box(
              contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color(0xFFdd5f6b))
                    .fillMaxHeight(0.5f)
                    .fillMaxWidth()
            ) {
               Icon(imageVector = Icons.Filled.Cancel, contentDescription = "cancel", modifier = Modifier.size(55.dp))
            }

            Column(
                verticalArrangement= Arrangement.Center,
                horizontalAlignment= Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
            ) {
                Text(
                    text = dialogMessage,
                    modifier = Modifier.padding(bottom = 15.dp),
                    fontSize = 18.sp
                    )
                Button(
                    onClick = onOkClick,
                    modifier = Modifier
                        .height(35.dp)
                        .width(80.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFdd5f6b), contentColor = Color.White),
                    shape = RoundedCornerShape(10.dp)
                ) {
                     Text(text = "OK")
                }
            }

        }
      }
    }
}

@Preview(device = "id:pixel_6_pro")
@Composable
fun ErrorDialogPreview() {
    ErrorDialog( onOkClick = {}, isVisible = true)
}
