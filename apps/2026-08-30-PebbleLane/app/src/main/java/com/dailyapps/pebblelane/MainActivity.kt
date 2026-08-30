package com.dailyapps.pebblelane

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.pebblelane.ui.navigation.PebbleNav
import com.dailyapps.pebblelane.ui.theme.PebbleTheme
import com.dailyapps.pebblelane.viewmodel.PebbleViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PebbleTheme {
                val vm: PebbleViewModel = viewModel()
                PebbleNav(vm)
            }
        }
    }
}
