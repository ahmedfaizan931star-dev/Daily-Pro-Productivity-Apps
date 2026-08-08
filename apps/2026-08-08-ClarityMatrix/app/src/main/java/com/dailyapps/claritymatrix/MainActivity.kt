package com.dailyapps.claritymatrix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.claritymatrix.ui.navigation.ClarityNavGraph
import com.dailyapps.claritymatrix.ui.theme.ClarityMatrixTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClarityMatrixTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ClarityNavGraph()
                }
            }
        }
    }
}
