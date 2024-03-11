package com.anas.aiassistant.presentaion.main

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.anas.aiassistant.data.AppData
import com.anas.aiassistant.data.AppData.suggestions
import com.anas.aiassistant.domain.viewModel.MainViewModel
import com.anas.aiassistant.presentaion.ErrorDialog
import com.anas.aiassistant.presentaion.LoadingSpinner
import com.anas.aiassistant.presentaion.bottomCard.BottomCard
import com.anas.aiassistant.presentaion.rememberImeState
import com.anas.aiassistant.shared.MainScreenEvent


@SuppressLint("MissingPermission")
@Composable
fun MainScreen(navController: NavHostController?) {
    val mainViewModel = hiltViewModel<MainViewModel>()

    val state by mainViewModel.state.collectAsState()
    val textFieldState by mainViewModel.textFieldState.collectAsState()
    // removing keyboard overlap the ui by listing the screen size changes
    val imaState = rememberImeState()
    LaunchedEffect(Unit){
        mainViewModel.init()
        mainViewModel.chatList = AppData.chats
    }
    val scrollState = rememberScrollState()

    // scroll down all the way to the bottom of screen when the keyboard is open
    LaunchedEffect(key1 = imaState.value){
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column (
        verticalArrangement= Arrangement.Top,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Astra AI",
            style = TextStyle(
                fontSize = 36.sp,
                brush = Brush.linearGradient(
                    colors = listOf(Color(0xFF5982de), Color.Red),

                ),

            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
           LazyRow{
               items(suggestions){ suggestion ->
                  SuggestionCard(text = suggestion, mainViewModel::onEvent)
               }
           }

        Button(
            onClick = { mainViewModel.onEvent(MainScreenEvent.OnRecentClick(navController!!))  },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 7.dp),

        ) {

            Row(
                horizontalArrangement= Arrangement.SpaceBetween,
                verticalAlignment= Alignment.CenterVertically,

                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Recent",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Go to recent",
                    modifier = Modifier
                        .height(31.dp)
                        .width(31.dp)
                )

            }
        }

        for (recentItem in mainViewModel.chatList.take(3)){ // take the 3 most recent chats
            RecentRow(recentItem.title, recentItem.id, navController)
        }
        Spacer(modifier = Modifier.weight(1f)) // Pushes content up
        BottomCard(navController!!, mainViewModel::onEvent, textFieldState)
    }

    // display an error dialog when needed
    ErrorDialog(onOkClick = {mainViewModel.onEvent(MainScreenEvent.OnOkErrorDialogClick)}, isVisible = state.isErrorDialogShown )

    // display a loading spinner
    LoadingSpinner(isVisible = state.isScreenLoading)

}


@Preview(showBackground = false, device = "id:pixel_6_pro")
@Composable
fun MainScreenPreview(){
    MainScreen(null)
}
