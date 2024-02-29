package com.anas.aiassistant.presentaion.main

import ai.picovoice.porcupine.Porcupine
import ai.picovoice.porcupine.PorcupineManager
import android.annotation.SuppressLint
import android.speech.SpeechRecognizer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.anas.aiassistant.data.AppData.suggestions
import com.anas.aiassistant.domain.viewModel.MainViewModel
import com.anas.aiassistant.presentaion.ErrorDialog
import com.anas.aiassistant.presentaion.LoadingSpinner
import com.anas.aiassistant.presentaion.bottomCard.BottomCard
import com.anas.aiassistant.presentaion.rememberImeState


@SuppressLint("MissingPermission")
@Composable
fun MainScreen(navController: NavHostController?) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val context = LocalContext.current

    // removing keyboard overlap the ui by listing the screen size changes
    val imaState = rememberImeState()
    val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    val scrollState = rememberScrollState()
    LaunchedEffect(Unit){
        mainViewModel.init()
    }

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
                  SuggestionCard(text = suggestion, mainViewModel)
               }
           }

        Button(
            onClick = { navController?.navigate("recent_chats_screen")  },
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

        for (recentItem in mainViewModel.chatList.takeLast(3)){
            RecentRow(recentItem.title, recentItem.id, navController)
        }
        Spacer(modifier = Modifier.weight(1f)) // Pushes content up
        BottomCard(mainViewModel, navController!!)
    }

    // display an error dialog when needed
    ErrorDialog(onOkClick = { mainViewModel.isErrorDialogShown = !mainViewModel.isErrorDialogShown}, isVisible = mainViewModel.isErrorDialogShown )

    // display a loading spinner
    LoadingSpinner(isVisible = mainViewModel.chatGbtLoading)
    val keywords = arrayOf(Porcupine.BuiltInKeyword.PORCUPINE, Porcupine.BuiltInKeyword.BUMBLEBEE)

    val accessKey = "xt5oThE5dlH78GexSdzPrXH6mRr+QGF0KsD57D77o7LddS9mWvivyw=="
    val keywordPath = "file://${context.assets}/Astra_en_android_v3_0_0.ppn"

    val porcupineManager = PorcupineManager.Builder()
        .setAccessKey(accessKey)
        .setKeyword(Porcupine.BuiltInKeyword.BUMBLEBEE)
        .build(LocalContext.current) { keywordIndex ->
            if (keywordIndex == 0) {
                // BUMBLEBEE detected
                mainViewModel.onMickClick(speechRecognizer)
            }
        }

    LaunchedEffect(Unit) {
        // Audio Setup
//        porcupineManager.start()
    }
}


@Preview(showBackground = false, device = "id:pixel_6_pro")
@Composable
fun MainScreenPreview(){
    MainScreen(null)
}
