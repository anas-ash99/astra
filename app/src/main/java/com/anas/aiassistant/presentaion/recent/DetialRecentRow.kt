package com.anas.aiassistant.presentaion.recent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.anas.aiassistant.presentaion.CircularIconButton
import com.anas.aiassistant.shared.RecentChatsScreenEvent
import kotlinx.coroutines.delay


@Composable
fun DetailRecentRow(
    label:String,
    isLabelVisible:Boolean = false,
    messageTitle:String,
    onItemClick:()->Unit,
    onEvent:(RecentChatsScreenEvent) ->Unit
) {


    var contextMenuExpanded by remember { mutableStateOf(false) }
    val moreVertIcon = rememberVectorPainter(image = Icons.Outlined.MoreVert)

    Column (
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .background(Color.White)
    ){
        if (isLabelVisible){
            Text(text = label)
        }

        ClickableBox(onClick = onItemClick) {
            Row(
                horizontalArrangement= Arrangement.Start,
                verticalAlignment= Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp)

            ) {
                Surface(
                    color = Color.LightGray,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp)
                            .padding(5.dp),
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription ="chat image"
                    )

                }

                Text(
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .weight(1f),
                    fontSize = 18.sp
                    ,
                    text = messageTitle
                )

                CircularIconButton(
                    onClick = { contextMenuExpanded = true },
                    icon = moreVertIcon ,
                    contentDescription = "more actions",
                    backgroundColor = Color.White
                )
            }

            DropdownMenu(
                expanded = contextMenuExpanded,
                onDismissRequest = { contextMenuExpanded = false },
                offset = DpOffset(x = LocalConfiguration.current.screenWidthDp.dp - 150.dp, y = (-10).dp)
            ) {
                DropdownMenuItem(text = { Text(text = "Delete") }, onClick = {
                    onEvent(RecentChatsScreenEvent.OnDeleteItemClick("hi this is an id "))
                    contextMenuExpanded = false
                })
                DropdownMenuItem(text = { Text(text = "Pin") }, onClick = {
                    contextMenuExpanded = false
                    onEvent(RecentChatsScreenEvent.OnPinItemClick("hi this is an id "))
                })
            }


        }

    }
}

@Composable
fun ClickableBox(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .clickable {
                isClicked = !isClicked
                onClick()
            }
            .background(if (isClicked) Color.LightGray else Color.White)
    ) {
        content()
    }

    LaunchedEffect(isClicked){
        if (isClicked){
            delay(100)
            isClicked = false
        }
    }
}

@Preview
@Composable
fun DetailRecentRowPreview() {
//    DetailRecentRow("today", false, "hello world", ){}
}




