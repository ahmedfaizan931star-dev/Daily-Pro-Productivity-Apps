package com.dailyapps.harborrite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.harborrite.ui.navigation.HarborNav
import com.dailyapps.harborrite.ui.theme.HarborRiteTheme
import com.dailyapps.harborrite.viewmodel.HarborViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HarborRiteTheme {
                val vm: HarborViewModel = viewModel()
                HarborNav(vm)
            }
        }
    }
}
