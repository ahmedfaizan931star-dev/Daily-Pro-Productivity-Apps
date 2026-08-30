package com.dailyapps.ridgenote.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ridgenote.viewmodel.RidgeViewModel
import kotlin.math.abs

@Composable
fun CalibrationScreen(vm: RidgeViewModel) {
    val s by vm.uiState.collectAsState()
    val story = when {
        s.reviewed == 0 -> "Review a few decisions to see calibration."
        abs(s.calibrationGap) <= 8 -> "Well calibrated. Confidence tracks outcomes."
        s.calibrationGap > 8 -> "Overconfident. Hit rate lags stated confidence."
        else -> "Underconfident. You hit more often than you expect."
    }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Calibration", style = MaterialTheme.typography.headlineLarge)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Hit rate ${s.hitRate}%")
                LinearProgressIndicator(progress = { s.hitRate / 100f }, modifier = Modifier.fillMaxWidth())
                Text("Average confidence ${s.avgConfidence}%")
                LinearProgressIndicator(progress = { s.avgConfidence / 100f }, modifier = Modifier.fillMaxWidth())
                Text("Gap ${s.calibrationGap} pts · ${s.reviewed} reviewed")
            }
        }
        Text(story, style = MaterialTheme.typography.bodyLarge)
    }
}
