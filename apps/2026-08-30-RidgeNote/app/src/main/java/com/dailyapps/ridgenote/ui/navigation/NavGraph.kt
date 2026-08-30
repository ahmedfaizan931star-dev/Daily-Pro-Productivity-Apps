package com.dailyapps.ridgenote.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.ridgenote.ui.screens.CalibrationScreen
import com.dailyapps.ridgenote.ui.screens.HomeScreen
import com.dailyapps.ridgenote.ui.screens.LogScreen
import com.dailyapps.ridgenote.ui.screens.ReviewScreen
import com.dailyapps.ridgenote.viewmodel.RidgeViewModel

@Composable
fun RidgeNav(vm: RidgeViewModel = viewModel()) {
    val nav = rememberNavController()
    val back by nav.currentBackStackEntryAsState()
    val route = back?.destination?.route ?: "home"
    val tabs = listOf(
        Triple("home", "Home", Icons.Outlined.Home),
        Triple("log", "Log", Icons.Outlined.Add),
        Triple("review", "Review", Icons.Outlined.RateReview),
        Triple("cal", "Calibrate", Icons.Outlined.Insights)
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEach { (r, label, icon) ->
                    NavigationBarItem(
                        selected = route == r,
                        onClick = { nav.navigate(r) { launchSingleTop = true } },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { HomeScreen(vm) }
            composable("log") { LogScreen(vm) }
            composable("review") { ReviewScreen(vm) }
            composable("cal") { CalibrationScreen(vm) }
        }
    }
}
