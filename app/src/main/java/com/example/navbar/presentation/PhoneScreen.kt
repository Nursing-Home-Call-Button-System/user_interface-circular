package com.example.navbar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun PhoneScreen(navController: NavHostController) {
    val config = LocalConfiguration.current

    // Detect round screen (WearOS)
    val isRound = config.screenWidthDp == config.screenHeightDp

    Box(
        modifier = Modifier
            .size(if (isRound) 180.dp else 320.dp) // Ensures UI fits within a 1.4-inch round screen
            .background(Color(0xff28AF3E)), // Green background for Phone Screen
    )
}
