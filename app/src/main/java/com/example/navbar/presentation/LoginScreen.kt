package com.example.navbar.presentation

import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {
    val config = LocalConfiguration.current
    val isRound = config.screenWidthDp == config.screenHeightDp

    var patientName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val backgroundColor = Color(0xFFF5F5DC)  // Soft Beige
    val buttonColor = Color(0xFF4682B4)  // Steel Blue
    val successColor = Color(0xFF3CB371) // Medium Sea Green
    val buttonTextColor = Color.White
    val textColor = Color.Black

    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    // 🔹 Authenticate the user when the screen loads
    LaunchedEffect(Unit) {
        authenticateUser(auth)
    }

    fun savePatientData(name: String, room: String) {
        if (auth.currentUser == null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Authentication failed, please try again")
            }
            return
        }

        if (name.isBlank() || room.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Please enter both fields")
            }
            return
        }

        val patientData = hashMapOf(
            "name" to name,
            "roomNumber" to room,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("patients")
            .add(patientData)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Document added with ID: ${documentReference.id}")

                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Logged in and saved to Firebase!")

                    Log.d("Navigation", "Attempting to navigate to home/$name/$room") // ✅ Debugging

                    navController.navigate("home/$name/$room") // ✅ Matches route in NavigationGraph
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error adding document", e)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error saving data")
                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Patient Profile",
                        fontSize = 16.sp,
                        color = textColor,
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Name", fontSize = 14.sp, color = textColor) },
                        placeholder = { Text("Enter name", fontSize = 14.sp, color = Color.Gray) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    OutlinedTextField(
                        value = roomNumber,
                        onValueChange = { roomNumber = it },
                        label = { Text("Room #", fontSize = 14.sp, color = textColor) },
                        placeholder = { Text("Enter room", fontSize = 14.sp, color = Color.Gray) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = {
                                if (patientName.isNotBlank() && roomNumber.isNotBlank()) {
                                    savePatientData(patientName, roomNumber) // ✅ Calling the function correctly
                                } else {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Please enter both fields")
                                    }
                                }
                            },
                            modifier = Modifier.width(100.dp).height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                        ) {
                            Text(text = "Login", fontSize = 14.sp, color = buttonTextColor)
                        }

                        Button(
                            onClick = { navController.navigate("signup_screen") },
                            modifier = Modifier
                                .width(100.dp)
                                .height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = successColor)
                        ) {
                            Text(text = "Sign Up", fontSize = 14.sp, color = buttonTextColor)
                        }
                    }
                }
            }
        }
    )
}

// 🔹 Function to Authenticate User Anonymously
fun authenticateUser(auth: FirebaseAuth) {
    if (auth.currentUser == null) {
        auth.signInAnonymously()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Auth", "Anonymous sign-in successful")
                } else {
                    Log.e("Auth", "Authentication failed", task.exception)
                }
            }
    } else {
        Log.d("Auth", "User already authenticated")
    }
}
