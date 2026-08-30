package com.dailyapps.ridgenote.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ridgenote.viewmodel.RidgeViewModel

@Composable
fun ReviewScreen(vm: RidgeViewModel) {
    val state by vm.uiState.collectAsState()
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Review outcomes", style = MaterialTheme.typography.headlineLarge)
        Text("Mark whether the call was a hit, miss, or mixed.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.decisions, key = { it.id }) { d ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(d.title, style = MaterialTheme.typography.titleLarge)
                        Text("${d.choice} · ${d.confidence}% · ${d.domain}")
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("hit", "miss", "mixed").forEach { o ->
                                AssistChip(onClick = { vm.setOutcome(d, o) }, label = { Text(o) })
                            }
                            TextButton(onClick = { vm.delete(d) }) { Text("Delete") }
                        }
                    }
                }
            }
        }
    }
}
