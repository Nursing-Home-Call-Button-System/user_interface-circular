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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun WearableNavigationBarWithScreens(patientViewModel: PatientViewModel = viewModel()) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableStateOf(0) }

    // ✅ Retrieve patient data from ViewModel
    val patientName by patientViewModel.patientName.collectAsState()
    val roomNumber by patientViewModel.roomNumber.collectAsState()

    Scaffold(
        timeText = { TimeText() },
        vignette = { Vignette(vignettePosition = VignettePosition.TopAndBottom) }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            NavigationGraph(navController)

            // ✅ Bottom Navigation Bar
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ✅ Home Button - Now with SAFE navigation
                    CompactChip(
                        onClick = {
                            selectedItem = 0
                            val safePatientName = if (patientName.isNotBlank()) patientName else "Unknown"
                            val safeRoomNumber = if (roomNumber.isNotBlank()) roomNumber else "N/A"

                            // 🔥 Proper URL Encoding
                            val encodedPatientName = URLEncoder.encode(safePatientName, StandardCharsets.UTF_8.toString())
                            val encodedRoomNumber = URLEncoder.encode(safeRoomNumber, StandardCharsets.UTF_8.toString())

                            navController.navigate("home/$encodedPatientName/$encodedRoomNumber") {
                                popUpTo(navController.graph.startDestinationRoute!!) { inclusive = false }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(10.dp)
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier
                            .width(48.dp)
                            .height(36.dp)
                    )

                    // ✅ Phone Button (Emergency)
                    CompactChip(
                        onClick = {
                            selectedItem = 1
                            navController.navigate("phone")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone",
                                modifier = Modifier.size(10.dp)
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier
                            .width(48.dp)
                            .height(36.dp)
                    )

                    // ✅ Settings Button
                    CompactChip(
                        onClick = {
                            selectedItem = 2
                            navController.navigate("settings")
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                modifier = Modifier.size(10.dp)
                            )
                        },
                        colors = ChipDefaults.primaryChipColors(Color.DarkGray),
                        modifier = Modifier
                            .width(48.dp)
                            .height(36.dp)
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
        composable("login_screen") { LoginScreen(navController) }
        composable("signup_screen") { SignUpScreen(navController) }
        composable("phone") { EmergencyScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("emergency") { EmergencyScreen(navController) }
        composable("non_emergency") { NonEmergencyScreen(navController) }

        // ✅ Home Navigation with Encoded Parameters
        composable(
            route = "home/{patientName}/{roomNumber}",
            arguments = listOf(
                navArgument("patientName") { type = NavType.StringType },
                navArgument("roomNumber") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val patientName = backStackEntry.arguments?.getString("patientName") ?: "Unknown"
            val roomNumber = backStackEntry.arguments?.getString("roomNumber") ?: "N/A"

            Log.d("Navigation", "✅ Navigated to HomeScreen - Patient: $patientName, Room: $roomNumber")
            HomeScreen(navController, patientName, roomNumber)
        }
    }
}
