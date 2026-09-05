package com.dailyapps.ashlarhours

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.dailyapps.ashlarhours.ui.screens.AshlarApp
import com.dailyapps.ashlarhours.ui.theme.AshlarHoursTheme

class MainActivity : ComponentActivity() {
    private val viewModel: AshlarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AshlarHoursTheme {
                val state by viewModel.state.collectAsState()
                AshlarApp(
                    state = state,
                    onSelectHour = viewModel::selectHour,
                    onTitle = viewModel::setDraftTitle,
                    onKind = viewModel::setDraftKind,
                    onPlace = viewModel::placeStone,
                    onToggle = viewModel::toggleDone,
                    onClear = viewModel::clearHour,
                    onTemplate = viewModel::applyTemplate,
                    onTarget = viewModel::setDeepTarget
                )
            }
        }
    }
}
