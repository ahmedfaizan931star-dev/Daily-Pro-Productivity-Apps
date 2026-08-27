package com.dailyapps.tidequota.ui.navigation

import android.app.Application
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.tidequota.ui.screens.DriftScreen
import com.dailyapps.tidequota.ui.screens.HarborScreen
import com.dailyapps.tidequota.ui.screens.LogScreen
import com.dailyapps.tidequota.ui.screens.QuotasScreen
import com.dailyapps.tidequota.viewmodel.TideViewModel

private data class Dest(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun TideNavGraph() {
    val nav = rememberNavController()
    val app = LocalContext.current.applicationContext as Application
    val vm: TideViewModel = viewModel(factory = TideViewModel.factory(app))
    val state by vm.uiState.collectAsStateWithLifecycle()
    val items = listOf(
        Dest("harbor", "Harbor", Icons.Outlined.Home),
        Dest("quotas", "Quotas", Icons.Outlined.Tune),
        Dest("log", "Log", Icons.Outlined.EditNote),
        Dest("drift", "Drift", Icons.Outlined.Analytics)
    )
    val back by nav.currentBackStackEntryAsState()
    val current = back?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { dest ->
                    NavigationBarItem(
                        selected = current == dest.route,
                        onClick = { nav.navigate(dest.route) { launchSingleTop = true } },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = { Text(dest.label) }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = "harbor",
            modifier = Modifier.padding(padding)
        ) {
            composable("harbor") { HarborScreen(state) }
            composable("quotas") { QuotasScreen(state, vm::setQuota) }
            composable("log") { LogScreen(state, vm::log, vm::delete) }
            composable("drift") { DriftScreen(state) }
        }
    }
}
