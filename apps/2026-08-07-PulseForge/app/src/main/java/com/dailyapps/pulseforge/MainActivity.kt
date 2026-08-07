package com.dailyapps.pulseforge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.pulseforge.ui.navigation.PulseNavGraph
import com.dailyapps.pulseforge.ui.theme.PulseForgeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PulseForgeTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PulseNavGraph()
                }
            }
        }
    }
}
