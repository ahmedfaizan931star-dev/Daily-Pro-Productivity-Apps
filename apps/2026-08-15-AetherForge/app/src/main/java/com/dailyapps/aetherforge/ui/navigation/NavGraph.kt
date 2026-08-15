package com.dailyapps.aetherforge.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.aetherforge.ui.screens.FocusScreen
import com.dailyapps.aetherforge.ui.screens.HomeScreen
import com.dailyapps.aetherforge.ui.screens.InsightsScreen
import com.dailyapps.aetherforge.ui.screens.TasksScreen
import com.dailyapps.aetherforge.viewmodel.AetherViewModel

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Tasks : Screen("tasks", "Tasks")
    data object Focus : Screen("focus", "Focus")
    data object Insights : Screen("insights", "Insights")
}

@Composable
fun AetherNavGraph(vm: AetherViewModel = viewModel()) {
    val navController = rememberNavController()
    val items = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.Tasks to Icons.Default.Checklist,
        Screen.Focus to Icons.Default.Timer,
        Screen.Insights to Icons.Default.Insights
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { (screen, icon) ->
                    NavigationBarItem(
                        icon = { Icon(icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(vm) }
            composable(Screen.Tasks.route) { TasksScreen(vm) }
            composable(Screen.Focus.route) { FocusScreen(vm) }
            composable(Screen.Insights.route) { InsightsScreen(vm) }
        }
    }
}
