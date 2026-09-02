package com.dailyapps.vespergate.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.vespergate.data.EveningEntity

@Composable
fun LedgerScreen(evenings: List<EveningEntity>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Evening ledger", style = MaterialTheme.typography.headlineSmall)
        Text(
            "A quiet record of how you left each day.",
            modifier = Modifier.padding(bottom = 12.dp),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        if (evenings.isEmpty()) {
            Text("No evenings sealed yet. Tonight is a good place to start.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(evenings, key = { it.dateKey }) { e ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(e.dateKey, style = MaterialTheme.typography.titleMedium)
                            Text("Score ${e.score}/5 · ${if (e.sealed) "sealed" else "open"}")
                            if (e.intention.isNotBlank()) Text("First move: ${e.intention}")
                            if (e.note.isNotBlank()) Text(e.note, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}
