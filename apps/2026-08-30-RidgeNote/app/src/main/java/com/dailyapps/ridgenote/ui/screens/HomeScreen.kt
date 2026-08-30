package com.dailyapps.ridgenote.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ridgenote.viewmodel.RidgeViewModel

@Composable
fun HomeScreen(vm: RidgeViewModel) {
    val state by vm.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("RidgeNote", style = MaterialTheme.typography.headlineLarge)
        Text("Decision journal with calibrated confidence.")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Pending", "${state.pending}", Modifier.weight(1f))
            StatCard("Hit rate", "${state.hitRate}%", Modifier.weight(1f))
            StatCard("Avg conf", "${state.avgConfidence}%", Modifier.weight(1f))
        }
        Text("Recent", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.decisions.take(8), key = { it.id }) { d ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text(d.title, style = MaterialTheme.typography.titleLarge)
                        Text("${d.domain} · ${d.confidence}% · ${d.outcome}")
                        if (d.choice.isNotBlank()) Text("Chose: ${d.choice}")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(value, style = MaterialTheme.typography.headlineLarge)
        }
    }
}
