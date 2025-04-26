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
        if (name.isBlank() || room.isBlank()) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar("Please enter both fields")
            }
            return
        }

        auth.signInAnonymously()
            .addOnSuccessListener { authResult ->
                val userId = authResult.user?.uid

                val patientData = hashMapOf(
                    "uid" to userId,
                    "name" to name,
                    "roomNumber" to room,
                    "timestamp" to System.currentTimeMillis()
                )

                db.collection("patients")
                    .document(userId ?: "")
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
            .addOnFailureListener { e ->
                Log.e("Auth", "❌ Anonymous login failed", e)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Authentication failed, please try again")
                }
            }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
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
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Patient Profile",
                    fontSize = 16.sp,
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = patientName,
                    onValueChange = { patientName = it },
                    label = { Text("Name") },
                    placeholder = { Text("Enter name") },
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 10.sp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp)
                )

                OutlinedTextField(
                    value = roomNumber,
                    onValueChange = { roomNumber = it },
                    label = { Text("Room #") },
                    placeholder = { Text("Enter room") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 10.sp),
                    modifier = Modifier
                        .width(150.dp)
                        .height(48.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { savePatientData(patientName, roomNumber) },
                        modifier = Modifier
                            .width(90.dp)
                            .height(42.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4682B4))
                    ) {
                        Text(text = "Login", fontSize = 8.sp, color = Color.White)
                    }

                    Button(
                        onClick = { navController.navigate("signup_screen") },
                        modifier = Modifier
                            .width(90.dp)
                            .height(42.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3CB371))
                    ) {
                        Text(text = "Sign Up", fontSize = 8.sp, color = Color.White)
                    }
                }
            }
        }
    }
}
