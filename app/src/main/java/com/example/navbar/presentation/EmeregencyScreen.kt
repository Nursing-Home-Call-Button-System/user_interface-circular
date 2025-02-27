package com.example.navbar.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
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
import java.io.File

@Composable
fun EmergencyScreen(navController: NavHostController) {
    val context = LocalContext.current
    val config = LocalConfiguration.current

    val isRound = config.screenWidthDp == config.screenHeightDp // Detect round screen

    val audioFile = File(context.externalCacheDir, "emergency_message.3gp")
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var isPlaying by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
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
        containerColor = Color(0xffCC0000) // Red background for emergency
    ) { _ ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffCC0000)) // Red background
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp) // Ensures even spacing
            ) {
                Text(
                    text = "Emergency Message",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                // Row for Start and Stop Recording
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (hasRecordPermission) {
                                startRecording(audioFile, context) { recorder -> mediaRecorder = recorder }
                                isRecording = true
                            } else {
                                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
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
                            stopRecording(mediaRecorder)
                            isRecording = false
                        },
                        enabled = isRecording,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        Text("Stop", fontSize = 14.sp)
                    }
                }

                // Row for Play and Stop Playback
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

// Function to Start Recording
fun startRecording(audioFile: File, context: android.content.Context, onRecorderReady: (MediaRecorder) -> Unit) {
    try {
        val mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
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

// Function to Stop Recording
fun stopRecording(mediaRecorder: MediaRecorder?) {
    try {
        mediaRecorder?.apply {
            stop()
            release()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// Function to Play the Recorded Audio
fun playRecording(audioFile: File, context: android.content.Context, onPlayerReady: (MediaPlayer) -> Unit) {
    try {
        val mediaPlayer = MediaPlayer().apply {
            setDataSource(audioFile.absolutePath)
            prepare()
            start()
            setOnCompletionListener {
                onPlayerReady(this) // Set playback state properly
            }
        }
        onPlayerReady(mediaPlayer)
        Toast.makeText(context, "Playing Recording", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Error playing recording: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

// Function to Stop Playback
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
