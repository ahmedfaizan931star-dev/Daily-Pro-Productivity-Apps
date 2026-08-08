package com.dailyapps.claritymatrix.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.GridView
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
import com.dailyapps.claritymatrix.ui.screens.InsightsScreen
import com.dailyapps.claritymatrix.ui.screens.MatrixScreen
import com.dailyapps.claritymatrix.ui.screens.TimerScreen
import com.dailyapps.claritymatrix.viewmodel.ClarityViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Matrix : Screen("matrix", "Matrix", Icons.Default.GridView)
    data object Timer : Screen("timer", "Focus", Icons.Default.Timer)
    data object Insights : Screen("insights", "Insights", Icons.Default.Analytics)
}

@Composable
fun ClarityNavGraph(viewModel: ClarityViewModel = viewModel()) {
    val navController = rememberNavController()
    val items = listOf(Screen.Matrix, Screen.Timer, Screen.Insights)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
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
            startDestination = Screen.Matrix.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Matrix.route) {
                MatrixScreen(viewModel = viewModel)
            }
            composable(Screen.Timer.route) {
                TimerScreen(viewModel = viewModel)
            }
            composable(Screen.Insights.route) {
                InsightsScreen(viewModel = viewModel)
            }
        }
    }
}
