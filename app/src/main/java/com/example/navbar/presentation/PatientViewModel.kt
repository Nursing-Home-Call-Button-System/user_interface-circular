package com.example.navbar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PatientViewModel : ViewModel() {
    private val _patientName = MutableStateFlow("")
    val patientName: StateFlow<String> = _patientName

    private val _roomNumber = MutableStateFlow("")
    val roomNumber: StateFlow<String> = _roomNumber

    fun clearPatientData() {
        _patientName.value = ""  // ✅ Reset patient name
        _roomNumber.value = ""   // ✅ Reset room number
        Log.d("PatientViewModel", "✅ Patient data cleared successfully")
    }
    fun setPatientData(name: String, room: String) {
        _patientName.value = name
        _roomNumber.value = room
    }
}
