package com.dailyapps.pebblelane.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.dailyapps.pebblelane.viewmodel.PebbleViewModel

@Composable
fun CloseoutScreen(vm: PebbleViewModel) {
    val items by vm.closeouts.collectAsState()
    var wins by remember { mutableStateOf("") }
    var leftover by remember { mutableStateOf("") }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Evening close-out")
        OutlinedTextField(wins, { wins = it }, label = { Text("What landed") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(leftover, { leftover = it }, label = { Text("Parked for tomorrow") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            if (wins.isNotBlank() || leftover.isNotBlank()) {
                vm.addCloseout(wins.trim(), leftover.trim())
                wins = ""
                leftover = ""
            }
        }) { Text("Close the day") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.id }) { item ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(item.wins.ifBlank { "—" })
                        Text(item.leftover.ifBlank { "Nothing parked" })
                    }
                }
            }
        }
    }
}
