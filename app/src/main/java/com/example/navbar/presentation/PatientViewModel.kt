package com.example.navbar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientViewModel : ViewModel() {

    private val _patientName = MutableStateFlow("")
    val patientName: StateFlow<String> = _patientName

    private val _roomNumber = MutableStateFlow("")
    val roomNumber: StateFlow<String> = _roomNumber

    init {
        loadPatientData()  // ✅ Load patient data automatically
    }

    fun clearPatientData() {
        _patientName.value = ""  // ✅ Reset patient name
        _roomNumber.value = ""   // ✅ Reset room number
        Log.d("PatientViewModel", "✅ Patient data cleared successfully")
    }

    fun setPatientData(name: String, room: String) {
        _patientName.value = name
        _roomNumber.value = room
        Log.d("PatientViewModel", "✅ Patient data set successfully: Name=$name, Room=$room")
    }

    // ✅ Load patient data (Replace this with your actual loading logic, e.g., Firebase)
    private fun loadPatientData() {
        viewModelScope.launch {
            // For testing purposes, replace these with real data fetches:
            val fetchedPatientName = "John Doe"
            val fetchedRoomNumber = "101"

            // Set fetched data
            _patientName.value = fetchedPatientName
            _roomNumber.value = fetchedRoomNumber

            Log.d("PatientViewModel", "✅ Loaded patient data: Name=$fetchedPatientName, Room=$fetchedRoomNumber")
        }
    }
}
