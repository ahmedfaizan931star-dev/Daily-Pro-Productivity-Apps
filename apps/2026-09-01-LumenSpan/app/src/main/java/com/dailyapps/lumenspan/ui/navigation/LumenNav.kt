package com.dailyapps.lumenspan.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.lumenspan.ui.screens.CheckInScreen
import com.dailyapps.lumenspan.ui.screens.InsightsScreen
import com.dailyapps.lumenspan.ui.screens.TodayScreen
import com.dailyapps.lumenspan.viewmodel.LumenViewModel

@Composable
fun LumenNav(vm: LumenViewModel) {
    val nav = rememberNavController()
    val back by nav.currentBackStackEntryAsState()
    val route = back?.destination?.route ?: "today"

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = route == "today",
                    onClick = { nav.navigate("today") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Today, contentDescription = null) },
                    label = { Text("Today") }
                )
                NavigationBarItem(
                    selected = route == "checkin",
                    onClick = { nav.navigate("checkin") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.LocalFireDepartment, contentDescription = null) },
                    label = { Text("Check-in") }
                )
                NavigationBarItem(
                    selected = route == "insights",
                    onClick = { nav.navigate("insights") { launchSingleTop = true } },
                    icon = { Icon(Icons.Outlined.Insights, contentDescription = null) },
                    label = { Text("Insights") }
                )
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "today",
            modifier = Modifier.padding(padding)
        ) {
            composable("today") { TodayScreen(vm) }
            composable("checkin") { CheckInScreen(vm) }
            composable("insights") { InsightsScreen(vm) }
        }
    }
}
