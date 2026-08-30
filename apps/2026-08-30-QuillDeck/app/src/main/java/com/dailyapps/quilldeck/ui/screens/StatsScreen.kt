package com.dailyapps.quilldeck.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dailyapps.quilldeck.viewmodel.QuillViewModel

@Composable
fun StatsScreen(vm: QuillViewModel) {
    val state by vm.ui.collectAsStateWithLifecycle()
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Retention", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("${state.retentionPct}% estimated hold rate")
                LinearProgressIndicator(
                    progress = state.retentionPct / 100f,
                    modifier = Modifier.fillMaxWidth()
                )
                Text("${state.reviewedToday} cards have at least one review")
                Text("${state.due.size} waiting in the due queue")
            }
        }
        Text("Intervals grow when you mark Good or Easy. Again resets the card to a one-day loop.")
    }
}
