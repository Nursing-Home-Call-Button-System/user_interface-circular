package com.example.navbar.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun HomeScreen(navController: NavController, patientName: String, roomNumber: String) {
    val config = LocalConfiguration.current
    val isRound = config.screenWidthDp == config.screenHeightDp

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(bottom = 18.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display patient information
            Text(
                text = "Patient: $patientName",
                color = Color.White,
                fontSize = 18.sp
            )

            Text(
                text = "Room: $roomNumber",
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Emergency Button
            Button(
                onClick = {
                    sendAlertToFirebase(patientName, roomNumber, "Emergency")
                    navController.navigate("emergency")
                },
                modifier = Modifier.size(140.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFCC0000))
            ) {
                Text(text = "Emergency", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Non-Emergency Button
            Button(
                onClick = {
                    sendAlertToFirebase(patientName, roomNumber, "Non-Emergency")
                    navController.navigate("non_emergency")
                },
                modifier = Modifier.size(140.dp, 50.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00B4D8))
            ) {
                Text(text = "Non-Emergency", color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}

// ✅ Function to Send Alerts to Firebase
fun sendAlertToFirebase(patientName: String, roomNumber: String, alertType: String) {
    val db = FirebaseFirestore.getInstance()

    val alertData = hashMapOf(
        "patientName" to patientName,
        "roomNumber" to roomNumber,
        "alertType" to alertType,
        "timestamp" to System.currentTimeMillis()
    )

    db.collection("alerts")
        .add(alertData)
        .addOnSuccessListener {
            println("Alert sent successfully!")
        }
        .addOnFailureListener { e ->
            println("Error sending alert: ${e.message}")
        }
}
