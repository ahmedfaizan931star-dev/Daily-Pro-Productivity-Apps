package com.dailyapps.ledgermesa.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ledgermesa.viewmodel.LedgerViewModel

@Composable
fun LogScreen(vm: LedgerViewModel) {
    val ui by vm.state.collectAsState()
    var selected by remember { mutableLongStateOf(ui.envelopes.firstOrNull()?.envelope?.id ?: 0L) }
    var amount by remember { mutableStateOf("12") }
    var note by remember { mutableStateOf("") }
    var open by remember { mutableStateOf(false) }
    val label = ui.envelopes.firstOrNull { it.envelope.id == selected }?.envelope?.name ?: "Choose envelope"
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Log a spend")
            OutlinedButton(onClick = { open = true }, modifier = Modifier.fillMaxWidth()) { Text(label) }
            DropdownMenu(expanded = open, onDismissRequest = { open = false }) {
                ui.envelopes.forEach {
                    DropdownMenuItem(
                        text = { Text(it.envelope.name) },
                        onClick = {
                            selected = it.envelope.id
                            open = false
                        }
                    )
                }
            }
            OutlinedTextField(amount, { amount = it }, label = { Text("Amount $") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(note, { note = it }, label = { Text("Note") }, modifier = Modifier.fillMaxWidth())
            Button(onClick = {
                vm.addSpend(selected, amount.toIntOrNull() ?: 0, note)
                note = ""
            }) { Text("Save spend") }
        }
        items(ui.recent, key = { it.id }) { spend ->
            val env = ui.envelopes.firstOrNull { it.envelope.id == spend.envelopeId }?.envelope?.name ?: "Envelope"
            Column {
                Text("$env · $${spend.amountCents / 100}")
                if (spend.note.isNotBlank()) Text(spend.note)
            }
        }
    }
}
