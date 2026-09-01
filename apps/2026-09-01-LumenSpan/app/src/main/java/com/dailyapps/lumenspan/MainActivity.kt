package com.dailyapps.lumenspan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.lumenspan.ui.navigation.LumenNav
import com.dailyapps.lumenspan.ui.theme.LumenSpanTheme
import com.dailyapps.lumenspan.viewmodel.LumenViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumenSpanTheme {
                val vm: LumenViewModel = viewModel()
                LumenNav(vm)
            }
        }
    }
}
