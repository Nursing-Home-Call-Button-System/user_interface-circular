package com.example.navbar.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun WearableNavigationBarWithScreens() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val config = LocalConfiguration.current
    val isRound = config.screenWidthDp == config.screenHeightDp

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Navigation Graph
            NavigationGraph(navController)

            // Bottom Navigation Bar (Optimized for 450x450 screens)
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 22.dp) // Moves navbar slightly up for better visibility
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Home Button (Icon Only)
                    CompactChip(
                        onClick = {
                            selectedItem = 0
                            navController.navigate("home") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(16.dp) // Smaller icon size
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier.width(65.dp).weight(1f) // Adjusted width & weight
                    )

                    // Phone Button (Icon Only)
                    CompactChip(
                        onClick = {
                            selectedItem = 1
                            navController.navigate("phone") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone",
                                modifier = Modifier.size(16.dp) // Smaller icon size
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier.width(65.dp).weight(1f) // Adjusted width & weight
                    )

                    // Settings Button (Icon Only)
                    CompactChip(
                        onClick = {
                            selectedItem = 2
                            navController.navigate("settings") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.size(16.dp) // Smaller icon size
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier.width(65.dp).weight(1f) // Adjusted width & weight
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("phone") { EmergencyScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("emergency") { EmergencyScreen(navController) }
        composable("non_emergency") { NonEmergencyScreen(navController) }
        composable("profile_screen") { LoginScreen(navController) }
        composable("signup_screen") { SignUpScreen(navController) }
    }
}
