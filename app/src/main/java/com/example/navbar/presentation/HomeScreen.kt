package com.example.navbar.presentation

import android.util.Log
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
            // ✅ Display patient information
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

            // 🚨 Rectangular Emergency Button
            Button(
                onClick = {
                    sendAlertToFirebase(patientName, roomNumber, "Emergency")
                    navController.navigate("emergency")
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFCC0000)) // Red color
            ) {
                Text(text = "Emergency", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🏥 Rectangular Non-Emergency Button
            Button(
                onClick = {
                    sendAlertToFirebase(patientName, roomNumber, "Non-Emergency")
                    navController.navigate("non_emergency")
                },
                modifier = Modifier
                    .width(180.dp)
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF00B4D8)) // Blue color
            ) {
                Text(text = "Non-Emergency", color = Color.Black, fontSize = 16.sp)
            }
        }
    }
}


// ✅ Function to Send Alerts to Firestore
fun sendAlertToFirebase(patientName: String, roomNumber: String, alertType: String) {
    val db = FirebaseFirestore.getInstance()

    // ✅ Create an Alert object
    val alert = Alert(patientName, roomNumber, alertType)

    // ✅ Convert Alert object to a HashMap for Firestore
    val alertData = hashMapOf(
        "patientName" to alert.patientName,
        "roomNumber" to alert.roomNumber,
        "alertType" to alert.alertType,
        "timestamp" to alert.timestamp
    )

    // 🔍 Debugging: Log alert details before sending
    Log.d("FirestoreAlert", "🔥 Sending Alert: $alertData")

    db.collection("alerts")
        .add(alertData) // ✅ Firestore requires a HashMap, not a data class
        .addOnSuccessListener { documentRef ->
            Log.d("FirestoreAlert", "✅ Alert stored in Firestore! ID: ${documentRef.id}")
        }
        .addOnFailureListener { e ->
            Log.e("FirestoreAlert", "❌ Error sending alert: ${e.message}")
        }
}
