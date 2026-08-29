package com.dailyapps.kitebrief.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.kitebrief.data.model.Brief

@Composable
fun ShutdownScreen(brief: Brief?, onSave: (String, Int) -> Unit) {
    var note by remember(brief?.shutdownNote) { mutableStateOf(brief?.shutdownNote.orEmpty()) }
    var score by remember(brief?.landingScore) { mutableFloatStateOf((brief?.landingScore ?: 3).toFloat().coerceIn(1f, 5f)) }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Evening landing", style = MaterialTheme.typography.headlineLarge)
        Text("Reel the line in. Capture what actually happened.", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Shutdown note") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4
        )
        Spacer(Modifier.height(12.dp))
        Text("Landing score ${score.toInt()}/5")
        Slider(value = score, onValueChange = { score = it }, valueRange = 1f..5f, steps = 3)
        Button(onClick = { onSave(note, score.toInt()) }) {
            Text(if (brief?.shutDown == true) "Update landing" else "Close the day")
        }
        if (brief?.shutDown == true) {
            Spacer(Modifier.height(12.dp))
            Text("Day is landed. Sleep on it.")
        }
    }
}
