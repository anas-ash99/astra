package com.anas.aiassistant.presentaion.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.domain.viewModel.MainViewModel


@Composable
fun SuggestionCard(text:String, mainViewModel: MainViewModel){
    Surface (
        modifier = Modifier
            .size(width = 200.dp, height = 170.dp)
            .padding(10.dp)
            .clickable { mainViewModel.onSuggestionCardClick(text) },
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF0F3F8),
    ){
         Text(
             text = text,
             modifier = Modifier
                 .padding(12.dp)
         )
    }
}


@Preview(showBackground = true, device = "id:pixel_7_pro")
@Composable
fun SuggestionCardPreview(){
    SuggestionCard(text = "hello there", MainViewModel())
}