package com.example.navbar.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.compose.material.MaterialTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // Apply the Wear OS Material Theme
            MaterialTheme {
                // Call the WearableNavigationBarExample composable
                WearableNavigationBarWithScreens()
            }
        }
    }
}
