package com.example.navbar.presentation

data class Alert(
    val patientName: String = "",
    val roomNumber: String = "",
    val alertType: String = "", // "Emergency" or "Non-Emergency"
    val timestamp: Long = System.currentTimeMillis()
)

