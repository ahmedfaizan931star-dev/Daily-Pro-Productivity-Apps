package com.dailyapps.pulseforge.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Star
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.pulseforge.ui.screens.HabitsScreen
import com.dailyapps.pulseforge.ui.screens.HomeScreen
import com.dailyapps.pulseforge.ui.screens.PrioritiesScreen
import com.dailyapps.pulseforge.ui.screens.TimerScreen
import com.dailyapps.pulseforge.viewmodel.PulseViewModel

sealed class Screen(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Home : Screen("home", "Home", Icons.Filled.Home, Icons.Outlined.Home)
    data object Timer : Screen("timer", "Focus", Icons.Filled.Timer, Icons.Outlined.Timer)
    data object Habits : Screen("habits", "Habits", Icons.Filled.List, Icons.Outlined.List)
    data object Priorities : Screen("priorities", "Forge", Icons.Filled.Star, Icons.Outlined.Star)
}

val bottomNavItems = listOf(
    Screen.Home,
    Screen.Timer,
    Screen.Habits,
    Screen.Priorities
)

@Composable
fun PulseNavGraph() {
    val navController = rememberNavController()
    val viewModel: PulseViewModel = viewModel()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
                                contentDescription = screen.label
                            )
                        },
                        label = { Text(screen.label) },
                        selected = selected,
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
            composable(Screen.Home.route) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigateToTimer = {
                        navController.navigate(Screen.Timer.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(Screen.Timer.route) {
                TimerScreen(viewModel = viewModel)
            }
            composable(Screen.Habits.route) {
                HabitsScreen(viewModel = viewModel)
            }
            composable(Screen.Priorities.route) {
                PrioritiesScreen(viewModel = viewModel)
            }
        }
    }
}
