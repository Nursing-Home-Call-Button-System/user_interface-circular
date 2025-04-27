package com.example.navbar.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.util.UUID

@Composable
fun EmergencyScreen(navController: NavHostController) {
    val context = LocalContext.current
    val config = LocalConfiguration.current

    val audioFile = File(context.externalCacheDir, "emergency_message.aac")
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val requestRecordPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Microphone permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    val hasRecordPermission = ContextCompat.checkSelfPermission(
        context, Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    Scaffold(
        containerColor = Color(0xffCC0000)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xffCC0000)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Emergency Message",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (hasRecordPermission) {
                                startRecording(audioFile, context) { recorder -> mediaRecorder = recorder }
                                isRecording = true
                            } else {
                                requestRecordPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        enabled = !isRecording,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Start", fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            stopRecording(mediaRecorder) {
                                isRecording = false

                                Handler(Looper.getMainLooper()).postDelayed({
                                    uploadAudioToFirebase(audioFile, context,
                                        onSuccess = { downloadUrl ->
                                            Toast.makeText(context, "Audio uploaded successfully!", Toast.LENGTH_SHORT).show()

                                            saveAudioMetadataToFirestore(
                                                audioUrl = downloadUrl,
                                                patientName = "John Doe",
                                                roomNumber = "101",
                                                alertType = "Emergency"
                                            )
                                        },
                                        onFailure = { e ->
                                            Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                                        }
                                    )
                                }, 300)
                            }
                        },
                        enabled = isRecording,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Stop", fontSize = 14.sp)
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (audioFile.exists()) {
                                if (!isPlaying) {
                                    playRecording(audioFile, context) { player -> mediaPlayer = player }
                                    isPlaying = true
                                }
                            } else {
                                Toast.makeText(context, "No recording found!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        enabled = !isRecording && !isPlaying,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Play", fontSize = 14.sp)
                    }

                    Button(
                        onClick = {
                            stopPlayback(mediaPlayer)
                            isPlaying = false
                        },
                        enabled = isPlaying,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Stop", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

// Start recording with AAC
fun startRecording(audioFile: File, context: android.content.Context, onRecorderReady: (MediaRecorder) -> Unit) {
    try {
        val mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS) // AAC format
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)      // AAC encoder
            setOutputFile(audioFile.absolutePath)
            prepare()
            start()
        }
        onRecorderReady(mediaRecorder)
        Toast.makeText(context, "Recording Started", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error starting recording: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

// Stop recording
fun stopRecording(mediaRecorder: MediaRecorder?, onStopped: () -> Unit) {
    try {
        mediaRecorder?.apply {
            stop()
            release()
        }
        onStopped()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Play recording
fun playRecording(audioFile: File, context: android.content.Context, onPlayerReady: (MediaPlayer) -> Unit) {
    try {
        val mediaPlayer = MediaPlayer().apply {
            setDataSource(audioFile.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                onPlayerReady(this)
            }
        }
        onPlayerReady(mediaPlayer)
        Toast.makeText(context, "Playing Recording", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error playing recording: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

// Stop playback
fun stopPlayback(mediaPlayer: MediaPlayer?) {
    try {
        mediaPlayer?.apply {
            stop()
            release()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Upload to Firebase
fun uploadAudioToFirebase(audioFile: File, context: android.content.Context, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val audioRef = storageRef.child("audio_messages/${UUID.randomUUID()}.aac")

    if (audioFile.length() == 0L) {
        Toast.makeText(context, "Recorded file is empty! Please try again.", Toast.LENGTH_LONG).show()
        return
    }

    val uploadTask = audioRef.putFile(Uri.fromFile(audioFile))

    uploadTask.addOnSuccessListener {
        audioRef.downloadUrl.addOnSuccessListener { uri ->
            onSuccess(uri.toString())
        }
    }.addOnFailureListener { exception ->
        onFailure(exception)
    }
}

// Save metadata to Firestore
fun saveAudioMetadataToFirestore(audioUrl: String, patientName: String, roomNumber: String, alertType: String) {
    val db = FirebaseFirestore.getInstance()
    val alert = hashMapOf(
        "patientName" to patientName,
        "roomNumber" to roomNumber,
        "alertType" to alertType,
        "audioUrl" to audioUrl,
        "timestamp" to System.currentTimeMillis()
    )

    db.collection("alerts")
        .add(alert)
}
