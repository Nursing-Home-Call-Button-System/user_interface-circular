package com.example.navbar.presentation

import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun WearableNavigationBarWithScreens() {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }
    val config = LocalConfiguration.current

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // ✅ Calls the Fixed NavigationGraph
            NavigationGraph(navController)

            // Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 22.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ✅ Home Button - Now Passes Required Parameters
                    CompactChip(
                        onClick = {
                            selectedItem = 0
                            val patientName = "Guest"  // Replace with actual data if available
                            val roomNumber = "000"  // Replace with actual data if available
                            navController.navigate("home/$patientName/$roomNumber") {
                                popUpTo(navController.graph.startDestinationId) { inclusive = true }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier.width(65.dp).weight(1f)
                    )

                    // ✅ Phone Button
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
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier.width(65.dp).weight(1f)
                    )

                    // ✅ Settings Button
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
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier.width(65.dp).weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "profile_screen") {
        composable("profile_screen") { LoginScreen(navController) }
        composable("signup_screen") { SignUpScreen(navController) }
        composable("phone") { EmergencyScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("emergency") { EmergencyScreen(navController) }
        composable("non_emergency") { NonEmergencyScreen(navController) }

        // ✅ Fixing HomeScreen by providing default parameters
        composable(
            route = "home/{patientName}/{roomNumber}",
            arguments = listOf(
                navArgument("patientName") { type = NavType.StringType; defaultValue = "Guest" },
                navArgument("roomNumber") { type = NavType.StringType; defaultValue = "000" }
            )
        ) { backStackEntry ->
            val patientName = backStackEntry.arguments?.getString("patientName") ?: "Guest"
            val roomNumber = backStackEntry.arguments?.getString("roomNumber") ?: "000"

            // ✅ Debugging Log to Ensure Navigation Works
            Log.d("Navigation", "Navigated to HomeScreen with Patient: $patientName, Room: $roomNumber")

            HomeScreen(navController, patientName, roomNumber)
        }
    }
}
