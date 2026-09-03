package com.dailyapps.northline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dailyapps.northline.ui.screens.NorthlineApp
import com.dailyapps.northline.ui.theme.NorthlineTheme

class MainActivity : ComponentActivity() {
    private val viewModel: NorthlineViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NorthlineTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                val nav = rememberNavController()
                NorthlineApp(state = state, vm = viewModel, nav = nav)
            }
        }
    }
}
