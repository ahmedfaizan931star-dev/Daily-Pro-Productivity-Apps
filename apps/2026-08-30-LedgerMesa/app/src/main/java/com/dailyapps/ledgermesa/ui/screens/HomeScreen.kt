package com.dailyapps.ledgermesa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ledgermesa.viewmodel.LedgerViewModel

@Composable
fun HomeScreen(vm: LedgerViewModel) {
    val ui by vm.state.collectAsState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("LedgerMesa", style = MaterialTheme.typography.headlineSmall)
            Text("Remaining this week: $${ui.remainingCents / 100}")
        }
        items(ui.envelopes, key = { it.envelope.id }) { status ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(status.envelope.name, style = MaterialTheme.typography.titleMedium)
                    Text("$${status.spentCents / 100} of $${status.envelope.weeklyLimitCents / 100}")
                    LinearProgressIndicator(
                        progress = status.usedPct.coerceIn(0f, 1f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
