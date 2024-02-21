package com.anas.aiassistant.presentaion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.anas.aiassistant.ui.theme.AppMainColor


@Composable
fun LoadingSpinner(isVisible:Boolean) {
    if (!isVisible) return
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement= Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp),
            color = AppMainColor,
        )
    }
}