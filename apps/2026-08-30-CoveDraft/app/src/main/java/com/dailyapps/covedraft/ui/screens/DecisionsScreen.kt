package com.dailyapps.covedraft.ui.screens

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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.covedraft.viewmodel.CoveViewModel

@Composable
fun DecisionsScreen(vm: CoveViewModel) {
    val decisions by vm.decisions.collectAsState()
    var request by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Incoming asks get a verdict. Nothing stays implied.")
        OutlinedTextField(request, { request = it }, label = { Text("Request") }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Accept", "Defer", "Decline").forEach { v ->
                AssistChip(
                    onClick = {
                        if (request.isNotBlank()) {
                            vm.addDecision(request.trim(), v)
                            request = ""
                        }
                    },
                    label = { Text(v) }
                )
            }
        }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(decisions, key = { it.id }) { d ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(d.verdict + " · " + d.request)
                        TextButton(onClick = { vm.deleteDecision(d.id) }) { Text("Remove") }
                    }
                }
            }
        }
    }
}
