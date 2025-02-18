package com.example.navbar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isRound = config.screenWidthDp == config.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize() // Ensure full screen usage
            .background(Color(0xFFD9D9D9)), // Light gray background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate("profile_screen") // Navigate to Login Screen
                },
                modifier = Modifier.size(140.dp, 50.dp) // Keep button size optimized
            ) {
                Text(text = "Account")
            }
        }
    }
}

