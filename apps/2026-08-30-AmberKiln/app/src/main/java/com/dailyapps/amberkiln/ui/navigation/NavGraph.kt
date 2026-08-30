package com.dailyapps.amberkiln.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Whatshot
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
import com.dailyapps.amberkiln.ui.screens.CooldownScreen
import com.dailyapps.amberkiln.ui.screens.ForgeScreen
import com.dailyapps.amberkiln.ui.screens.KilnsScreen
import com.dailyapps.amberkiln.ui.screens.SessionsScreen
import com.dailyapps.amberkiln.viewmodel.AmberViewModel

private data class Tab(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun AmberNav(vm: AmberViewModel) {
    val nav = rememberNavController()
    val tabs = listOf(
        Tab("forge", "Forge", Icons.Default.Home),
        Tab("kilns", "Kilns", Icons.Default.Whatshot),
        Tab("fire", "Fire", Icons.Default.LocalFireDepartment),
        Tab("cool", "Cool", Icons.Default.Notes)
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
        NavHost(nav, startDestination = "forge", modifier = Modifier.padding(padding)) {
            composable("forge") { ForgeScreen(vm) }
            composable("kilns") { KilnsScreen(vm) }
            composable("fire") { SessionsScreen(vm) }
            composable("cool") { CooldownScreen(vm) }
        }
    }
}
