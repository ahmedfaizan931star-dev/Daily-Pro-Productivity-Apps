package com.dailyapps.covedraft.ui.screens

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
import com.dailyapps.covedraft.viewmodel.CoveViewModel

@Composable
fun CloseoutScreen(vm: CoveViewModel) {
    val closeouts by vm.closeouts.collectAsState()
    var win by remember { mutableStateOf("") }
    var leftover by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Name one win. Park what remains. Then stop.")
        OutlinedTextField(win, { win = it }, label = { Text("Today's win") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(leftover, { leftover = it }, label = { Text("Leftover to park") }, modifier = Modifier.fillMaxWidth())
        Button(onClick = {
            if (win.isNotBlank()) {
                vm.addCloseout(win.trim(), leftover.trim())
                if (leftover.isNotBlank()) vm.addDraft(leftover.trim(), "From evening close-out")
                win = ""
                leftover = ""
            }
        }) { Text("Close the day") }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(closeouts, key = { it.id }) { c ->
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(c.win)
                        if (c.leftover.isNotBlank()) Text("Parked: " + c.leftover)
                    }
                }
            }
        }
    }
}
