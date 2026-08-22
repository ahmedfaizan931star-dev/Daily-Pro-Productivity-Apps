package com.dailyapps.solsticeflow.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.solsticeflow.ui.screens.EnergyScreen
import com.dailyapps.solsticeflow.ui.screens.FocusScreen
import com.dailyapps.solsticeflow.ui.screens.HabitsScreen
import com.dailyapps.solsticeflow.ui.screens.HomeScreen
import com.dailyapps.solsticeflow.viewmodel.SolsticeViewModel

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Energy : Screen("energy", "Energy")
    data object Focus : Screen("focus", "Focus")
    data object Habits : Screen("habits", "Habits")
}

@Composable
fun SolsticeNavGraph(viewModel: SolsticeViewModel) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Energy, Screen.Focus, Screen.Habits)

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
                                    Screen.Energy -> Icons.Default.Insights
                                    Screen.Focus -> Icons.Default.Timer
                                    Screen.Habits -> Icons.Default.SelfImprovement
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
            composable(Screen.Energy.route) { EnergyScreen(viewModel) }
            composable(Screen.Focus.route) { FocusScreen(viewModel) }
            composable(Screen.Habits.route) { HabitsScreen(viewModel) }
        }
    }
}
