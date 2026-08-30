package com.dailyapps.quilldeck.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
fun HomeScreen(vm: QuillViewModel, onReview: () -> Unit) {
    val state by vm.ui.collectAsStateWithLifecycle()
    Column(
        Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("QuillDeck", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.SemiBold)
        Text("Keep knowledge in rotation. Review what is due, then get back to work.")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatChip("Due", "${state.due.size}", Modifier.weight(1f))
            StatChip("Cards", "${state.cards.size}", Modifier.weight(1f))
            StatChip("Retention", "${state.retentionPct}%", Modifier.weight(1f))
        }
        Button(onClick = onReview, enabled = state.due.isNotEmpty(), modifier = Modifier.fillMaxWidth()) {
            Text(if (state.due.isEmpty()) "Nothing due" else "Start ${state.due.size} cards")
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
    }
}
