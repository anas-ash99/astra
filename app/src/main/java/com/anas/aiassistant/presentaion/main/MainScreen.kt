package com.anas.aiassistant.presentaion.main

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.anas.aiassistant.domain.viewModel.MainViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navController: NavHostController?, viewModel: MainViewModel?) {


    Column (
        verticalArrangement= Arrangement.Top,
        horizontalAlignment = Alignment.Start,

        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())

    ) {
        Text(
            text = "Astra AI",
            style = TextStyle(
                color = Color(0xFF007AFF),
                fontSize = 36.sp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        )
           LazyRow{
               items(viewModel?.suggestions!!){ suggestion ->
                  SuggestionCard(text = suggestion, viewModel)
               }
           }

        Text(
            text = "Recent:",
            modifier = Modifier.padding(10.dp),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )


        for (recentItem in viewModel?.chatList?.takeLast(3)!!){
            RecentRow(recentItem.title, recentItem.id, navController)
        }
        Spacer(modifier = Modifier.weight(1f)) // Pushes content up
        BottomCard(viewModel, navController!!)
    }


}




@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = false, device = "id:pixel_6_pro")
@Composable
fun MainScreenPreview(){
    MainScreen(null, null)
}
