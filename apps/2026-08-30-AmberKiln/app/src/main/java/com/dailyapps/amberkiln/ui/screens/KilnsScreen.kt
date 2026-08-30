package com.dailyapps.amberkiln.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.dailyapps.amberkiln.viewmodel.AmberViewModel

@Composable
fun KilnsScreen(vm: AmberViewModel) {
    val state by vm.state.collectAsState()
    var name by remember { mutableStateOf("") }
    var intent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Kilns", style = MaterialTheme.typography.headlineSmall)
        OutlinedTextField(name, { name = it }, label = { Text("Project name") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(intent, { intent = it }, label = { Text("Intent") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            vm.addKiln(name, intent)
            name = ""
            intent = ""
        }) { Text("Open kiln") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(state.kilns, key = { it.id }) { kiln ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(kiln.name, style = MaterialTheme.typography.titleMedium)
                            Text(kiln.intent.ifBlank { "No intent" })
                            Text("Heat ${state.heatFor(kiln.id)}")
                        }
                        IconButton(onClick = { vm.deleteKiln(kiln) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}
