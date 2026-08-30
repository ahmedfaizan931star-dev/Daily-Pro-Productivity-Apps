package com.dailyapps.quilldeck.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Insights
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.quilldeck.ui.screens.DecksScreen
import com.dailyapps.quilldeck.ui.screens.HomeScreen
import com.dailyapps.quilldeck.ui.screens.ReviewScreen
import com.dailyapps.quilldeck.ui.screens.StatsScreen
import com.dailyapps.quilldeck.viewmodel.QuillViewModel

private data class Dest(val route: String, val label: String, val icon: ImageVector)

@Composable
fun QuillNav(vm: QuillViewModel) {
    val nav = rememberNavController()
    val items = listOf(
        Dest("home", "Home", Icons.Outlined.Home),
        Dest("review", "Review", Icons.Outlined.AutoStories),
        Dest("decks", "Decks", Icons.Outlined.Style),
        Dest("stats", "Stats", Icons.Outlined.Insights)
    )
    val back by nav.currentBackStackEntryAsState()
    val current = back?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach {
                    NavigationBarItem(
                        selected = current == it.route,
                        onClick = { nav.navigate(it.route) { launchSingleTop = true } },
                        icon = { Icon(it.icon, contentDescription = it.label) },
                        label = { Text(it.label) }
                    )
                }
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "home", modifier = Modifier.padding(pad)) {
            composable("home") { HomeScreen(vm) { nav.navigate("review") } }
            composable("review") { ReviewScreen(vm) }
            composable("decks") { DecksScreen(vm) }
            composable("stats") { StatsScreen(vm) }
        }
    }
}
