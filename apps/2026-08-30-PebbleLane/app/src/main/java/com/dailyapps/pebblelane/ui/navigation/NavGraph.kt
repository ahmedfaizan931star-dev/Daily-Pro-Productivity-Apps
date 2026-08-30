package com.dailyapps.pebblelane.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.pebblelane.ui.screens.BlocksScreen
import com.dailyapps.pebblelane.ui.screens.CloseoutScreen
import com.dailyapps.pebblelane.ui.screens.HarborScreen
import com.dailyapps.pebblelane.ui.screens.MeetingsScreen
import com.dailyapps.pebblelane.viewmodel.PebbleViewModel

private data class Tab(val route: String, val label: String, val icon: ImageVector)

@Composable
fun PebbleNav(vm: PebbleViewModel) {
    val nav = rememberNavController()
    val tabs = listOf(
        Tab("harbor", "Harbor", Icons.Default.Home),
        Tab("meetings", "Meetings", Icons.Default.Event),
        Tab("blocks", "Blocks", Icons.Default.Lock),
        Tab("closeout", "Close-out", Icons.Default.Nightlight)
    )
    val back by nav.currentBackStackEntryAsState()
    val current = back?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = current == tab.route,
                        onClick = { nav.navigate(tab.route) { launchSingleTop = true } },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = "harbor", modifier = Modifier.padding(padding)) {
            composable("harbor") { HarborScreen(vm) }
            composable("meetings") { MeetingsScreen(vm) }
            composable("blocks") { BlocksScreen(vm) }
            composable("closeout") { CloseoutScreen(vm) }
        }
    }
}
