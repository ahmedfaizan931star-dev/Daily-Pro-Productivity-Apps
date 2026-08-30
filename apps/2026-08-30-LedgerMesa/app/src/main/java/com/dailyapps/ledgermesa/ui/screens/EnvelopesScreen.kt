package com.dailyapps.ledgermesa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ledgermesa.viewmodel.LedgerViewModel

@Composable
fun EnvelopesScreen(vm: LedgerViewModel) {
    val ui by vm.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var dollars by remember { mutableStateOf("40") }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Fund a weekly envelope")
            OutlinedTextField(name, { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(dollars, { dollars = it }, label = { Text("Weekly $ ") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                vm.addEnvelope(name, dollars.toIntOrNull() ?: 0)
                name = ""
            }) { Text("Add envelope") }
        }
        items(ui.envelopes, key = { it.envelope.id }) { status ->
            Column(Modifier.fillMaxWidth()) {
                Text(status.envelope.name)
                Text("Limit $${status.envelope.weeklyLimitCents / 100} · left $${status.remainingCents / 100}")
            }
        }
    }
}
