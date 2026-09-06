package com.dailyapps.emberwake

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.emberwake.ui.EmberViewModel
import com.dailyapps.emberwake.ui.screens.Destinations
import com.dailyapps.emberwake.ui.screens.InsightsScreen
import com.dailyapps.emberwake.ui.screens.NavIcons
import com.dailyapps.emberwake.ui.screens.RitualScreen
import com.dailyapps.emberwake.ui.screens.TodayScreen
import com.dailyapps.emberwake.ui.theme.EmberWakeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EmberWakeTheme {
                EmberApp()
            }
        }
    }
}

@Composable
fun EmberApp(vm: EmberViewModel = viewModel()) {
    val state by vm.ui.collectAsStateWithLifecycle()
    val nav = rememberNavController()
    val back by nav.currentBackStackEntryAsState()
    val current = back?.destination?.route ?: Destinations.Today

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavIcons.forEach { (route, label, icon) ->
                    NavigationBarItem(
                        selected = current == route,
                        onClick = { nav.navigate(route) { launchSingleTop = true } },
                        icon = { Icon(icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Destinations.Today,
            modifier = Modifier.padding(padding)
        ) {
            composable(Destinations.Today) {
                TodayScreen(
                    state = state,
                    onEnergy = vm::setEnergy,
                    onDraft = vm::setDraft,
                    onAdd = vm::addCommitment,
                    onToggle = vm::toggleCommit,
                    onRemove = vm::removeCommit,
                    onSnapshot = vm::snapshotToday
                )
            }
            composable(Destinations.Wake) {
                RitualScreen(
                    title = "Wake ritual",
                    subtitle = "Five quiet moves before the inbox.",
                    steps = state.wake,
                    onToggle = vm::toggleWake
                )
            }
            composable(Destinations.Shut) {
                RitualScreen(
                    title = "Shutdown",
                    subtitle = "Close the loop so tomorrow starts clean.",
                    steps = state.shutdown,
                    onToggle = vm::toggleShut
                )
            }
            composable(Destinations.Insights) {
                InsightsScreen(state)
            }
        }
    }
}
