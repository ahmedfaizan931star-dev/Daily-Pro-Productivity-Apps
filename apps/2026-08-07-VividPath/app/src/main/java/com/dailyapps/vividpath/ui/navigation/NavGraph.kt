package com.dailyapps.vividpath.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
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
import com.dailyapps.vividpath.ui.screens.FocusScreen
import com.dailyapps.vividpath.ui.screens.HomeScreen
import com.dailyapps.vividpath.ui.screens.PathScreen
import com.dailyapps.vividpath.ui.screens.ReflectScreen
import com.dailyapps.vividpath.viewmodel.VividViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Path : Screen("path", "Path", Icons.Default.ListAlt)
    data object Focus : Screen("focus", "Focus", Icons.Default.Timer)
    data object Reflect : Screen("reflect", "Reflect", Icons.Default.EditNote)
}

val bottomNavItems = listOf(Screen.Home, Screen.Path, Screen.Focus, Screen.Reflect)

@Composable
fun VividNavGraph(viewModel: VividViewModel = viewModel()) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { screen ->
                    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
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
                HomeScreen(viewModel = viewModel)
            }
            composable(Screen.Path.route) {
                PathScreen(viewModel = viewModel)
            }
            composable(Screen.Focus.route) {
                FocusScreen(viewModel = viewModel)
            }
            composable(Screen.Reflect.route) {
                ReflectScreen(viewModel = viewModel)
            }
        }
    }
}
