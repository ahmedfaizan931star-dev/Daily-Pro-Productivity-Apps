package com.dailyapps.stillpoint.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.stillpoint.ui.screens.FocusScreen
import com.dailyapps.stillpoint.ui.screens.HomeScreen
import com.dailyapps.stillpoint.ui.screens.InsightsScreen
import com.dailyapps.stillpoint.ui.screens.QuietScreen
import com.dailyapps.stillpoint.viewmodel.StillViewModel

private data class Dest(val route: String, val label: String, val icon: ImageVector)

private val dests = listOf(
    Dest("home", "Harbor", Icons.Outlined.Home),
    Dest("quiet", "Quiet", Icons.Outlined.SelfImprovement),
    Dest("focus", "Still", Icons.Outlined.Timer),
    Dest("insights", "Pulse", Icons.Outlined.Insights)
)

@Composable
fun StillNav(vm: StillViewModel = viewModel()) {
    val nav = rememberNavController()
    val state by vm.state.collectAsStateWithLifecycle()
    val back by nav.currentBackStackEntryAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                dests.forEach { d ->
                    val selected = back?.destination?.hierarchy?.any { it.route == d.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            nav.navigate(d.route) {
                                popUpTo("home") { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(d.icon, contentDescription = d.label) },
                        label = { Text(d.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") { HomeScreen(state, vm) }
            composable("quiet") { QuietScreen(state, vm) }
            composable("focus") { FocusScreen(state, vm) }
            composable("insights") { InsightsScreen(state) }
        }
    }
}
