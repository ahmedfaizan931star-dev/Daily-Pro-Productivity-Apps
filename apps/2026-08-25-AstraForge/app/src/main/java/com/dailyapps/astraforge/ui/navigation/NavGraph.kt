package com.dailyapps.astraforge.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Timer
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
import com.dailyapps.astraforge.ui.screens.FocusScreen
import com.dailyapps.astraforge.ui.screens.HabitsScreen
import com.dailyapps.astraforge.ui.screens.HomeScreen
import com.dailyapps.astraforge.ui.screens.TasksScreen
import com.dailyapps.astraforge.viewmodel.AstraViewModel

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Focus : Screen("focus", "Focus")
    data object Habits : Screen("habits", "Habits")
    data object Tasks : Screen("tasks", "Tasks")
}

@Composable
fun AstraNavGraph(viewModel: AstraViewModel = viewModel()) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Focus, Screen.Habits, Screen.Tasks)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = when (screen) {
                                    Screen.Home -> Icons.Default.Home
                                    Screen.Focus -> Icons.Default.Timer
                                    Screen.Habits -> Icons.Default.CheckCircle
                                    Screen.Tasks -> Icons.Default.List
                                },
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
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
            composable(Screen.Home.route) { HomeScreen(viewModel) }
            composable(Screen.Focus.route) { FocusScreen(viewModel) }
            composable(Screen.Habits.route) { HabitsScreen(viewModel) }
            composable(Screen.Tasks.route) { TasksScreen(viewModel) }
        }
    }
}
