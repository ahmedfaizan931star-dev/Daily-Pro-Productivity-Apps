package com.dailyapps.nexusflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.nexusflow.ui.navigation.NexusNavGraph
import com.dailyapps.nexusflow.ui.theme.NexusFlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexusFlowTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    NexusNavGraph()
                }
            }
        }
    }
}
