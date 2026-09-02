package com.dailyapps.vespergate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Nightlight
import androidx.compose.material.icons.outlined.WbTwilight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dailyapps.vespergate.ui.GateViewModel
import com.dailyapps.vespergate.ui.screens.GateScreen
import com.dailyapps.vespergate.ui.screens.LedgerScreen
import com.dailyapps.vespergate.ui.screens.LoopsScreen
import com.dailyapps.vespergate.ui.theme.VesperGateTheme

class MainActivity : ComponentActivity() {
    private val viewModel: GateViewModel by viewModels { GateViewModel.factory(application) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VesperGateTheme {
                val nav = rememberNavController()
                val back by nav.currentBackStackEntryAsState()
                val route = back?.destination?.route ?: "gate"
                val state by viewModel.state.collectAsStateWithLifecycle()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = route == "gate",
                                onClick = { nav.navigate("gate") { launchSingleTop = true } },
                                icon = { Icon(Icons.Outlined.Nightlight, contentDescription = null) },
                                label = { Text("Gate") }
                            )
                            NavigationBarItem(
                                selected = route == "loops",
                                onClick = { nav.navigate("loops") { launchSingleTop = true } },
                                icon = { Icon(Icons.Outlined.WbTwilight, contentDescription = null) },
                                label = { Text("Loops") }
                            )
                            NavigationBarItem(
                                selected = route == "ledger",
                                onClick = { nav.navigate("ledger") { launchSingleTop = true } },
                                icon = { Icon(Icons.Outlined.History, contentDescription = null) },
                                label = { Text("Ledger") }
                            )
                        }
                    }
                ) { padding ->
                    NavHost(
                        navController = nav,
                        startDestination = "gate",
                        modifier = Modifier.padding(padding)
                    ) {
                        composable("gate") {
                            GateScreen(
                                state = state,
                                onSaveNote = viewModel::saveNote,
                                onSaveIntention = viewModel::saveIntention,
                                onToggleRitual = viewModel::toggleRitual,
                                onScore = viewModel::setScore,
                                onSeal = viewModel::sealTonight
                            )
                        }
                        composable("loops") {
                            LoopsScreen(
                                loops = state.loops,
                                onAdd = viewModel::addLoop,
                                onPark = viewModel::parkLoop,
                                onClose = viewModel::closeLoop,
                                onDelete = viewModel::deleteLoop
                            )
                        }
                        composable("ledger") {
                            LedgerScreen(evenings = state.evenings)
                        }
                    }
                }
            }
        }
    }
}
