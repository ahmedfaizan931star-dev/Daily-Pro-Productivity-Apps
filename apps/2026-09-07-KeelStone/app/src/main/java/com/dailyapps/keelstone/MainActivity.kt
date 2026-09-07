package com.dailyapps.keelstone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.dailyapps.keelstone.ui.KeelApp
import com.dailyapps.keelstone.ui.theme.KeelStoneTheme

class MainActivity : ComponentActivity() {
    private val vm: KeelViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeelStoneTheme {
                val state by vm.state.collectAsStateWithLifecycle()
                val nav = rememberNavController()
                KeelApp(state = state, vm = vm, nav = nav)
            }
        }
    }
}
