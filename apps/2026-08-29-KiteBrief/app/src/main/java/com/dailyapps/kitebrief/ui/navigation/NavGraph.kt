package com.dailyapps.kitebrief.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.kitebrief.ui.screens.CommitmentsScreen
import com.dailyapps.kitebrief.ui.screens.HomeScreen
import com.dailyapps.kitebrief.ui.screens.ShutdownScreen
import com.dailyapps.kitebrief.ui.screens.WindMapScreen
import com.dailyapps.kitebrief.viewmodel.KiteViewModel

@Composable
fun KiteNav(vm: KiteViewModel) {
    val nav = rememberNavController()
    val brief by vm.brief.collectAsStateWithLifecycle()
    val commitments by vm.commitments.collectAsStateWithLifecycle()
    val recent by vm.recent.collectAsStateWithLifecycle()
    val back by nav.currentBackStackEntryAsState()
    val route = back?.destination?.route ?: "home"

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = route == "home",
                    onClick = { nav.navigate("home") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Home, null) },
                    label = { Text("Brief") }
                )
                NavigationBarItem(
                    selected = route == "commit",
                    onClick = { nav.navigate("commit") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Flag, null) },
                    label = { Text("Lift") }
                )
                NavigationBarItem(
                    selected = route == "land",
                    onClick = { nav.navigate("land") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Nightlight, null) },
                    label = { Text("Land") }
                )
                NavigationBarItem(
                    selected = route == "wind",
                    onClick = { nav.navigate("wind") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Air, null) },
                    label = { Text("Wind") }
                )
            }
        }
    ) { padding ->
        NavHost(navController = nav, startDestination = "home", modifier = Modifier.padding(padding)) {
            composable("home") {
                HomeScreen(
                    brief = brief,
                    commitments = commitments,
                    onSaveIntention = vm::saveIntention,
                    onOpenCommitments = { nav.navigate("commit") },
                    onOpenShutdown = { nav.navigate("land") },
                    onOpenWind = { nav.navigate("wind") }
                )
            }
            composable("commit") {
                CommitmentsScreen(
                    items = commitments,
                    onAdd = vm::addCommitment,
                    onToggle = vm::toggle,
                    onRemove = vm::remove
                )
            }
            composable("land") { ShutdownScreen(brief = brief, onSave = vm::shutdown) }
            composable("wind") { WindMapScreen(recent = recent) }
        }
    }
}
