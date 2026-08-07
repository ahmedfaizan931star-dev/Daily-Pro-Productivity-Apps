package com.dailyapps.zenithfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier.Modifier
import com.dailyapps.zenithfocus.ui.navigation.ZenithNavGraph
import com.dailyapps.zenithfocus.ui.theme.ZenithFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ZenithFocusTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ZenithNavGraph()
                }
            }
        }
    }
}
