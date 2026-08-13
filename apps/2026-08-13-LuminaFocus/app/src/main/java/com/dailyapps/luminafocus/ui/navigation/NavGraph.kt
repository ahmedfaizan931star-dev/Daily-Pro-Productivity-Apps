package com.dailyapps.luminafocus.ui.navigation

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.luminafocus.ui.screens.FocusScreen
import com.dailyapps.luminafocus.ui.screens.HabitsScreen
import com.dailyapps.luminafocus.ui.screens.HomeScreen
import com.dailyapps.luminafocus.ui.screens.InsightsScreen
import com.dailyapps.luminafocus.viewmodel.LuminaViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Focus : Screen("focus", "Focus", Icons.Default.Timer)
    data object Habits : Screen("habits", "Habits", Icons.Default.SelfImprovement)
    data object Insights : Screen("insights", "Insights", Icons.Default.Insights)
}

val bottomItems = listOf(Screen.Home, Screen.Focus, Screen.Habits, Screen.Insights)

@Composable
fun LuminaNavGraph(viewModel: LuminaViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
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
            composable(Screen.Insights.route) { InsightsScreen(viewModel) }
        }
    }
}
