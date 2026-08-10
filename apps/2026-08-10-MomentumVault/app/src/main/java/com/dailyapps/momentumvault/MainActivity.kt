package com.dailyapps.momentumvault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.momentumvault.ui.navigation.MomentumNavGraph
import com.dailyapps.momentumvault.ui.theme.MomentumVaultTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MomentumVaultTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MomentumNavGraph()
                }
            }
        }
    }
}
