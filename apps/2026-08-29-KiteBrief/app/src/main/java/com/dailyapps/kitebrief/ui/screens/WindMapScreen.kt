package com.dailyapps.kitebrief.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.kitebrief.data.model.Brief

@Composable
fun WindMapScreen(recent: List<Brief>) {
    val avgEnergy = recent.map { it.energy }.takeIf { it.isNotEmpty() }?.average() ?: 0.0
    val landed = recent.count { it.shutDown }
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Wind map", style = MaterialTheme.typography.headlineLarge)
        Text("Last ${recent.size} briefings · $landed landed · avg energy ${"%.1f".format(avgEnergy)}")
        Spacer(Modifier.height(16.dp))
        LazyColumn {
            items(recent, key = { it.dateKey }) { brief ->
                Card(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Column(Modifier.padding(14.dp)) {
                        Text(brief.dateKey, style = MaterialTheme.typography.titleMedium)
                        Text(brief.intention.ifBlank { "No intention logged" })
                        Spacer(Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = brief.energy / 5f,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (brief.shutDown) {
                            Text("Landed ${brief.landingScore}/5 · ${brief.shutdownNote}")
                        }
                    }
                }
            }
        }
    }
}
