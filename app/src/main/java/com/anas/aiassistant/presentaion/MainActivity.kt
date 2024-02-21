package com.anas.aiassistant.presentaion

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.anas.aiassistant.utilities.NavigationMap


class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent{
             NavigationMap()
        }
    }

//    @Deprecated("Deprecated in Java")
//    override fun onBackPressed() {
//        viewModel.messageTextFieldFocus = false
//        viewModel.messageTextInput = "Type, talk, or share a photo to Astra AI"
//        if (viewModel.isTextFieldCardShown){
//            viewModel.isTextFieldCardShown = false
//        }else{
//            super.onBackPressed()
//        }
//
//    }
}

