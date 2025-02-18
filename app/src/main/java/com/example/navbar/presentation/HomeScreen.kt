package com.example.navbar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Text

@Composable
fun HomeScreen(navController: NavController) {
    val config = LocalConfiguration.current

    // Detect round screen (WearOS)
    val isRound = config.screenWidthDp == config.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 18.dp), // Moves everything slightly up, but less than before
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center, // Keeps elements centered
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(7.dp)) // Moves Emergency button lower

            // Emergency Button
            Button(
                onClick = { navController.navigate("emergency") },
                modifier = Modifier.size(140.dp, 50.dp), // Button remains the same size
                colors = ButtonDefaults.buttonColors(
                     Color(0xFFCC0000)
                )
            ) {
                Text(
                    text = "Emergency",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Keeps good spacing between buttons

            // Non-Emergency Button
            Button(
                onClick = { navController.navigate("non_emergency") },
                modifier = Modifier.size(140.dp, 50.dp), // Button remains the same size
                colors = ButtonDefaults.buttonColors(
                 Color(0xFF00B4D8)
                )
            ) {
                Text(
                    text = "Non-Emergency",
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}
