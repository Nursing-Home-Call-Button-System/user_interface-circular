package com.example.navbar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isRound = config.screenWidthDp == config.screenHeightDp

    var patientName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor = Color(0xFFF5F5DC)  // Soft Beige for Comfort
    val buttonColor = Color(0xFF4682B4)  // Steel Blue for Better Contrast
    val buttonTextColor = Color.White  // White for High Contrast
    val textColor = Color.Black  // Dark Text for Readability

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize() // Fixes layout issue
                    .background(backgroundColor)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp), // Increased padding for spacing
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Patient Profile",
                        fontSize = 14.sp, // Increased for readability
                        color = textColor,
                        style = MaterialTheme.typography.labelLarge
                    )

                    // Name Input Field
                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Name", fontSize = 12.sp, color = textColor) },
                        placeholder = { Text("Enter name", fontSize = 12.sp, color = Color.Gray) },
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = textColor),
                        singleLine = true,
                        modifier = Modifier
                            .width(150.dp) // Adjusted width for better fit
                            .height(40.dp)
                    )

                    // Room Number Input Field
                    OutlinedTextField(
                        value = roomNumber,
                        onValueChange = { roomNumber = it },
                        label = { Text("Room #", fontSize = 12.sp, color = textColor) },
                        placeholder = { Text("Enter room", fontSize = 12.sp, color = Color.Gray) },
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 12.sp, color = textColor),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .width(150.dp) // Adjusted width for better fit
                            .height(40.dp)
                    )

                    // Button Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Login Button
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Logged in successfully!")
                                }
                            },
                            modifier = Modifier
                                .width(80.dp) // Adjusted button width
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text(text = "Login", fontSize = 12.sp, color = buttonTextColor)
                        }

                        // Sign Up Button
                        Button(
                            onClick = {
                                navController.navigate("signup_screen")
                            },
                            modifier = Modifier
                                .width(80.dp) // Adjusted button width
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CB371))
                        ) {
                            Text(text = "Sign Up", fontSize = 12.sp, color = buttonTextColor)
                        }
                    }
                }
            }
        }
    )
}
