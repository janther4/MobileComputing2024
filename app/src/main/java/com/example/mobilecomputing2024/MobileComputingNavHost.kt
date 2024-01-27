package com.example.mobilecomputing2024

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecomputing2024.ui.home.HomeScreen
import com.example.mobilecomputing2024.ui.messages.Conversation
import androidx.compose.ui.Modifier


@Composable
fun MobileComputingNavHost(modifier: Modifier = Modifier,
                           navController: NavHostController = rememberNavController(),
                           startDestination: String = "messages"
) {
    NavHost(modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ){
        composable("messages") {
            Conversation(onNavigateToHome = {navController.navigate("home")},
                SampleData.conversationSample)
        }
        composable("home"){
            HomeScreen(
                onNavigateToMessages = { navController.navigate("messages")}
            )
        }
    }

}