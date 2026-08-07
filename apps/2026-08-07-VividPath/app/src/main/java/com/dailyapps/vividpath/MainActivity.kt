package com.dailyapps.vividpath

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.vividpath.ui.navigation.VividNavGraph
import com.dailyapps.vividpath.ui.theme.VividPathTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VividPathTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    VividNavGraph()
                }
            }
        }
    }
}
