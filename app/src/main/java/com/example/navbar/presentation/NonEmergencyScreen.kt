package com.example.navbar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults

@Composable
fun NonEmergencyScreen(navController: NavHostController) {
    val config = LocalConfiguration.current

    // Detect round screen (WearOS)
    val isRound = config.screenWidthDp == config.screenHeightDp

    Box(
        modifier = Modifier
            .size(180.dp) // Ensures it fits within a 1.4-inch round screen
            .background(Color(0xFF00B4D8)), // Blue background
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Water Button
            Button(
                onClick = {},
                modifier = Modifier.size(140.dp, 50.dp), // Ensures proper fit
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
            ) {
                Text(text = "Water", color = Color.Black)
            }

            // Row for Other & Bathroom Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Other Button
                Button(
                    onClick = {},
                    modifier = Modifier.size(70.dp, 50.dp), // Ensures proper spacing
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(text = "Other", color = Color.Black)
                }

                // Bathroom Button
                Button(
                    onClick = {},
                    modifier = Modifier.size(70.dp, 50.dp), // Ensures proper spacing
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(text = "Bathroom", color = Color.Black)
                }
            }
        }
    }
}
