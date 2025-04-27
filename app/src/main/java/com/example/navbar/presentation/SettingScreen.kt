package com.example.navbar.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun SettingsScreen(navController: NavHostController, patientViewModel: PatientViewModel = viewModel()) {
    val config = LocalConfiguration.current
    val isRound = config.screenWidthDp == config.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFD9D9D9)), // Light gray background
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // ✅ Account Button
            Button(
                onClick = {
                    navController.navigate("profile_screen") // Navigate to Profile
                },
                modifier = Modifier.size(140.dp, 50.dp)
            ) {
                Text(text = "Account")
            }

            // ✅ Logout Button - FIXED NAVIGATION
            Button(
                onClick = {
                    Log.d("Logout", "🚀 Logout button clicked")

                    // ✅ Clear patient data
                    patientViewModel.clearPatientData()

                    // ✅ Ensure all navigation history is cleared
                    navController.popBackStack(navController.graph.startDestinationId, true)

                    // ✅ Navigate to login screen as the only active destination
                    navController.navigate("login_screen") {
                        launchSingleTop = true
                    }

                    // ✅ Ensure ViewModel is reset
                    navController.currentBackStackEntry?.savedStateHandle?.set("resetViewModel", true)
                },
                colors = ButtonDefaults.buttonColors(Color.Red),
                modifier = Modifier.size(140.dp, 50.dp)
            ) {
                Text(text = "Logout", color = Color.White)
            }

        }}}




