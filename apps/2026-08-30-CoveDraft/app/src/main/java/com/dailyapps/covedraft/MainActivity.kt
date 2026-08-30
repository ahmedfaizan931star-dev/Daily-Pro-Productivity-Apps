package com.dailyapps.covedraft

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dailyapps.covedraft.ui.navigation.CoveNav
import com.dailyapps.covedraft.ui.theme.CoveDraftTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CoveDraftTheme {
                CoveNav()
            }
        }
    }
}
