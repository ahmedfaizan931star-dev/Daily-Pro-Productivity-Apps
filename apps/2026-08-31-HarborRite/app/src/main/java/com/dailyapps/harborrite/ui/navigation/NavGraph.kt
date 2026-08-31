package com.dailyapps.harborrite.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Anchor
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Sailing
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.harborrite.ui.screens.BerthsScreen
import com.dailyapps.harborrite.ui.screens.HarborScreen
import com.dailyapps.harborrite.ui.screens.RitesScreen
import com.dailyapps.harborrite.ui.screens.VoyagesScreen
import com.dailyapps.harborrite.viewmodel.HarborViewModel

private data class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun HarborNav(vm: HarborViewModel) {
    val nav = rememberNavController()
    val tabs = listOf(
        Tab("harbor", "Harbor", Icons.Default.Home),
        Tab("berths", "Berths", Icons.Default.Anchor),
        Tab("voyages", "Voyages", Icons.Default.Sailing),
        Tab("rites", "Rites", Icons.Default.Notes)
    )
    val back by nav.currentBackStackEntryAsState()
    val current = back?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = current == tab.route,
                        onClick = {
                            nav.navigate(tab.route) {
                                popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(nav, startDestination = "harbor", modifier = Modifier.padding(padding)) {
            composable("harbor") { HarborScreen(vm) }
            composable("berths") { BerthsScreen(vm) }
            composable("voyages") { VoyagesScreen(vm) }
            composable("rites") { RitesScreen(vm) }
        }
    }
}
