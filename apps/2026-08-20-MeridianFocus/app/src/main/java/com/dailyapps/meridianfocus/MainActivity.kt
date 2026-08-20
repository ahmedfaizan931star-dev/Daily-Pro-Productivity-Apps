package com.dailyapps.meridianfocus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.meridianfocus.ui.navigation.MeridianNavGraph
import com.dailyapps.meridianfocus.ui.theme.MeridianFocusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MeridianFocusTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MeridianNavGraph()
                }
            }
        }
    }
}
