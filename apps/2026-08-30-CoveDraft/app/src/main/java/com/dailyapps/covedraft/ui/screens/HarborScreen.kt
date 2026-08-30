package com.dailyapps.covedraft.ui.screens

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
import com.dailyapps.covedraft.viewmodel.CoveViewModel

@Composable
fun HarborScreen(vm: CoveViewModel) {
    val drafts by vm.drafts.collectAsState()
    val decisions by vm.decisions.collectAsState()
    val closeouts by vm.closeouts.collectAsState()
    val parked = drafts.count { it.parked && !it.launched }
    val launched = drafts.count { it.launched }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("CoveDraft", style = MaterialTheme.typography.headlineMedium)
            Text("Park unfinished work. Decide incoming asks. Close the day.")
        }
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard("Parked", parked.toString(), Modifier.weight(1f))
                StatCard("Launch", launched.toString(), Modifier.weight(1f))
                StatCard("Verdicts", decisions.size.toString(), Modifier.weight(1f))
            }
        }
        item { Text("Tonight's leftovers", style = MaterialTheme.typography.titleMedium) }
        items(closeouts.take(3)) { c ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text("Win: ${c.win}", style = MaterialTheme.typography.titleSmall)
                    Text("Carry: ${c.leftover}")
                }
            }
        }
        if (closeouts.isEmpty()) {
            item { Text("No close-out yet. End the day from the Closeout tab.") }
        }
    }
}

@Composable
private fun StatCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier) {
        Column(Modifier.padding(12.dp)) {
            Text(label, style = MaterialTheme.typography.labelMedium)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
