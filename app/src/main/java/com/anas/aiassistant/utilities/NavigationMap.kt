package com.anas.aiassistant.utilities

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.anas.aiassistant.presentaion.chat.ChatScreen
import com.anas.aiassistant.presentaion.main.MainScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationMap() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main_screen") {
        composable("main_screen"){
            MainScreen(navController)
        }
        composable("chat_screen/{chatId}",
            arguments = listOf(
                navArgument("chatId"){
                     type = NavType.StringType
                }
            ),

        ){
           ChatScreen(navController)
        }
    }
}


