package com.dailyapps.cascadeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.CheckCircle
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
import com.dailyapps.cascadeflow.ui.screens.FocusScreen
import com.dailyapps.cascadeflow.ui.screens.HabitsScreen
import com.dailyapps.cascadeflow.ui.screens.HomeScreen
import com.dailyapps.cascadeflow.ui.screens.TasksScreen
import com.dailyapps.cascadeflow.ui.theme.CascadeFlowTheme
import com.dailyapps.cascadeflow.viewmodel.CascadeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CascadeFlowTheme {
                CascadeFlowApp()
            }
        }
    }
}

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    data object Home : Screen("home", "Home", Icons.Default.Home)
    data object Tasks : Screen("tasks", "Tasks", Icons.Default.List)
    data object Focus : Screen("focus", "Focus", Icons.Default.Timer)
    data object Habits : Screen("habits", "Habits", Icons.Default.CheckCircle)
}

@Composable
fun CascadeFlowApp(viewModel: CascadeViewModel = viewModel()) {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Tasks, Screen.Focus, Screen.Habits)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
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
            composable(Screen.Tasks.route) { TasksScreen(viewModel) }
            composable(Screen.Focus.route) { FocusScreen(viewModel) }
            composable(Screen.Habits.route) { HabitsScreen(viewModel) }
        }
    }
}
