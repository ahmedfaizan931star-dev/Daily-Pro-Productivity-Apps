package com.dailyapps.stillpoint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dailyapps.stillpoint.ui.navigation.StillNav
import com.dailyapps.stillpoint.ui.theme.StillpointTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StillpointTheme {
                StillNav()
            }
        }
    }
}
