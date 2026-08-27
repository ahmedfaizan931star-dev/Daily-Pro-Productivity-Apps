package com.dailyapps.tidequota

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.tidequota.ui.navigation.TideNavGraph
import com.dailyapps.tidequota.ui.theme.TideQuotaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TideQuotaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TideNavGraph()
                }
            }
        }
    }
}
