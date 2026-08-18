package com.dailyapps.synapsestack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.synapsestack.ui.navigation.SynapseNavGraph
import com.dailyapps.synapsestack.ui.theme.SynapseStackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SynapseStackTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SynapseNavGraph()
                }
            }
        }
    }
}
