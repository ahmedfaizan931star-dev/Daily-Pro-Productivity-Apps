package com.dailyapps.pebblelane.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.pebblelane.viewmodel.PebbleViewModel

@Composable
fun BlocksScreen(vm: PebbleViewModel) {
    val items by vm.blocks.collectAsState()
    var label by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("90") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Protected blocks")
        OutlinedTextField(label, { label = it }, label = { Text("Focus work") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(minutes, { minutes = it }, label = { Text("Minutes") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            val m = minutes.toIntOrNull() ?: return@Button
            if (label.isNotBlank()) {
                vm.addBlock(label.trim(), m)
                label = ""
            }
        }) { Text("Lock block") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(item.label)
                            Text("${item.minutes} min · ${if (item.done) "kept" else "locked"}")
                        }
                        IconButton(onClick = { vm.toggleBlock(item) }) {
                            Icon(Icons.Default.Check, contentDescription = "Toggle")
                        }
                        IconButton(onClick = { vm.deleteBlock(item) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
