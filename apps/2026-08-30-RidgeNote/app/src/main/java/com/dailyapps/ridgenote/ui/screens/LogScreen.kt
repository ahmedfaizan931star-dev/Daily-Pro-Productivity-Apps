package com.dailyapps.ridgenote.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dailyapps.ridgenote.viewmodel.RidgeViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LogScreen(vm: RidgeViewModel) {
    var title by remember { mutableStateOf("") }
    var context by remember { mutableStateOf("") }
    var choice by remember { mutableStateOf("") }
    var conf by remember { mutableFloatStateOf(70f) }
    var domain by remember { mutableStateOf("Work") }
    val domains = listOf("Work", "Career", "Money", "Health", "People", "Product")

    Column(
        Modifier.fillMaxSize().padding(20.dp).verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Log a decision", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(title, { title = it }, label = { Text("What is the decision?") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(context, { context = it }, label = { Text("Context / stakes") }, modifier = Modifier.fillMaxWidth(), minLines = 2)
        OutlinedTextField(choice, { choice = it }, label = { Text("The choice you made") }, modifier = Modifier.fillMaxWidth())
        Text("Confidence ${conf.toInt()}%")
        Slider(value = conf, onValueChange = { conf = it }, valueRange = 0f..100f)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            domains.forEach { d ->
                FilterChip(selected = domain == d, onClick = { domain = d }, label = { Text(d) })
            }
        }
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    vm.add(title, context, choice, conf.toInt(), domain)
                    title = ""; context = ""; choice = ""; conf = 70f
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = title.isNotBlank()
        ) { Text("Save decision") }
    }
}
