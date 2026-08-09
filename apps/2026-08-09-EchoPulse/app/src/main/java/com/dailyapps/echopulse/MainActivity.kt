package com.dailyapps.echopulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.echopulse.ui.navigation.EchoNavGraph
import com.dailyapps.echopulse.ui.theme.EchoPulseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EchoPulseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EchoNavGraph()
                }
            }
        }
    }
}
