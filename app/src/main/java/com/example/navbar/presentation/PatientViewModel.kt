package com.example.navbar.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class PatientViewModel : ViewModel() {

    private val _patientName = MutableStateFlow("")
    val patientName: StateFlow<String> = _patientName

    private val _roomNumber = MutableStateFlow("")
    val roomNumber: StateFlow<String> = _roomNumber

    init {
        // ❌ No automatic loading at startup
    }

    fun clearPatientData() {
        _patientName.value = ""
        _roomNumber.value = ""
        Log.d("PatientViewModel", "✅ Patient data cleared successfully")
    }

    fun setPatientData(name: String, room: String) {
        _patientName.value = name
        _roomNumber.value = room
        Log.d("PatientViewModel", "✅ Patient data set manually: Name=$name, Room=$room")
    }

    fun loadPatientData() {
        viewModelScope.launch {
            try {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId.isNullOrEmpty()) {
                    Log.e("PatientViewModel", "❌ No authenticated user found")
                    return@launch
                }

                val document = FirebaseFirestore.getInstance()
                    .collection("patients")
                    .document(userId)
                    .get()
                    .await()

                if (document.exists()) {
                    val fetchedPatientName = document.getString("name") ?: ""
                    val fetchedRoomNumber = document.getString("roomNumber") ?: ""

                    _patientName.value = fetchedPatientName
                    _roomNumber.value = fetchedRoomNumber

                    Log.d("PatientViewModel", "✅ Loaded patient data: Name=$fetchedPatientName, Room=$fetchedRoomNumber")
                } else {
                    Log.e("PatientViewModel", "❌ No patient document found for UID=$userId")
                }
            } catch (e: Exception) {
                Log.e("PatientViewModel", "❌ Failed to load patient data", e)
            }
        }
    }
}
