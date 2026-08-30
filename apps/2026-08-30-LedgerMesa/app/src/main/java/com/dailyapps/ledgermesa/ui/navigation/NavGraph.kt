package com.dailyapps.ledgermesa.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.ledgermesa.ui.screens.EnvelopesScreen
import com.dailyapps.ledgermesa.ui.screens.HomeScreen
import com.dailyapps.ledgermesa.ui.screens.LogScreen
import com.dailyapps.ledgermesa.ui.screens.RunwayScreen
import com.dailyapps.ledgermesa.viewmodel.LedgerViewModel

@Composable
fun LedgerNav(vm: LedgerViewModel) {
    val nav = rememberNavController()
    val back by nav.currentBackStackEntryAsState()
    val route = back?.destination?.route ?: "home"
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = route == "home",
                    onClick = { nav.navigate("home") },
                    icon = { Icon(Icons.Outlined.Home, null) },
                    label = { Text("Mesa") }
                )
                NavigationBarItem(
                    selected = route == "envelopes",
                    onClick = { nav.navigate("envelopes") },
                    icon = { Icon(Icons.Outlined.AccountBalanceWallet, null) },
                    label = { Text("Pots") }
                )
                NavigationBarItem(
                    selected = route == "log",
                    onClick = { nav.navigate("log") },
                    icon = { Icon(Icons.Outlined.ReceiptLong, null) },
                    label = { Text("Log") }
                )
                NavigationBarItem(
                    selected = route == "runway",
                    onClick = { nav.navigate("runway") },
                    icon = { Icon(Icons.Outlined.Timeline, null) },
                    label = { Text("Runway") }
                )
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { HomeScreen(vm) }
            composable("envelopes") { EnvelopesScreen(vm) }
            composable("log") { LogScreen(vm) }
            composable("runway") { RunwayScreen(vm) }
        }
    }
}
