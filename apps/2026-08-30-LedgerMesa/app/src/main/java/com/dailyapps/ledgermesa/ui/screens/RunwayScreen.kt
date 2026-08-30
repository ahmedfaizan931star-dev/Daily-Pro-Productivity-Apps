package com.dailyapps.ledgermesa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun RunwayScreen(vm: LedgerViewModel) {
    val ui by vm.state.collectAsState()
    val pct = if (ui.weekLimitCents == 0) 0f else (ui.weekSpentCents.toFloat() / ui.weekLimitCents).coerceIn(0f, 1f)
    Column(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Week runway", style = MaterialTheme.typography.headlineSmall)
        Text("Spent $${ui.weekSpentCents / 100} of $${ui.weekLimitCents / 100}")
        LinearProgressIndicator(progress = pct, modifier = Modifier.fillMaxWidth())
        val over = ui.envelopes.filter { it.remainingCents < 0 }
        if (over.isEmpty()) {
            Text("All envelopes are inside their weekly limit.")
        } else {
            Text("${over.size} envelope(s) over budget")
            over.forEach { Text("${it.envelope.name}: over by $${-it.remainingCents / 100}") }
        }
    }
}
