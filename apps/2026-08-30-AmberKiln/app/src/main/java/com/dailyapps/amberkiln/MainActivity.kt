package com.dailyapps.amberkiln

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.amberkiln.ui.navigation.AmberNav
import com.dailyapps.amberkiln.ui.theme.AmberKilnTheme
import com.dailyapps.amberkiln.viewmodel.AmberViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AmberKilnTheme {
                val vm: AmberViewModel = viewModel()
                AmberNav(vm)
            }
        }
    }
}
