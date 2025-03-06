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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController,
    patientViewModel: PatientViewModel = viewModel()
) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var patientName by remember { mutableStateOf("") }
    var roomNumber by remember { mutableStateOf("") }

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

        val userId = auth.currentUser!!.uid

        val patientData = hashMapOf(
            "uid" to userId,
            "name" to name,
            "roomNumber" to room,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection("patients")
            .document(userId)
            .set(patientData)
            .addOnSuccessListener {
                Log.d("Firestore", "✅ Patient data saved for UID: $userId")

                patientViewModel.setPatientData(name, room)

                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Login successful!")
                    navController.navigate("home/$name/$room") {
                        popUpTo("profile_screen") { inclusive = true }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "❌ Error saving data", e)
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
                    .background(Color(0xFFF5F5DC)) // Soft Beige
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
                        color = Color.Black,
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        label = { Text("Name") },
                        placeholder = { Text("Enter name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    OutlinedTextField(
                        value = roomNumber,
                        onValueChange = { roomNumber = it },
                        label = { Text("Room #") },
                        placeholder = { Text("Enter room") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { savePatientData(patientName, roomNumber) },
                            modifier = Modifier.width(100.dp).height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4682B4)) // Steel Blue
                        ) {
                            Text(text = "Login", fontSize = 14.sp, color = Color.White)
                        }

                        Button(
                            onClick = { navController.navigate("signup_screen") },
                            modifier = Modifier.width(100.dp).height(45.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CB371)) // Medium Sea Green
                        ) {
                            Text(text = "Sign Up", fontSize = 14.sp, color = Color.White)
                        }
                    }
                }
            }
        }
    )
}
