package com.dailyapps.covedraft.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Rule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.covedraft.ui.screens.CloseoutScreen
import com.dailyapps.covedraft.ui.screens.DecisionsScreen
import com.dailyapps.covedraft.ui.screens.DraftsScreen
import com.dailyapps.covedraft.ui.screens.HarborScreen
import com.dailyapps.covedraft.viewmodel.CoveViewModel

@Composable
fun CoveNav(vm: CoveViewModel = viewModel()) {
    val nav = rememberNavController()
    val route = nav.currentBackStackEntryAsState().value?.destination?.route
    val items = listOf(
        "harbor" to Icons.Outlined.Home,
        "drafts" to Icons.Outlined.Description,
        "decisions" to Icons.Outlined.Rule,
        "closeout" to Icons.Outlined.Bedtime
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { (r, icon) ->
                    NavigationBarItem(
                        selected = route == r,
                        onClick = { nav.navigate(r) { launchSingleTop = true } },
                        icon = { Icon(icon, contentDescription = r) },
                        label = { Text(r.replaceFirstChar { it.uppercase() }) }
                    )
                }
            }
        }
    ) { pad ->
        NavHost(nav, startDestination = "harbor", modifier = Modifier.padding(pad)) {
            composable("harbor") { HarborScreen(vm) }
            composable("drafts") { DraftsScreen(vm) }
            composable("decisions") { DecisionsScreen(vm) }
            composable("closeout") { CloseoutScreen(vm) }
        }
    }
}
