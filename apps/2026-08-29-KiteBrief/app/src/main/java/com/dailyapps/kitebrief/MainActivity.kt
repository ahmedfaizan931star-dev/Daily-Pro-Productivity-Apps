package com.dailyapps.kitebrief

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.dailyapps.kitebrief.ui.navigation.KiteNav
import com.dailyapps.kitebrief.ui.theme.KiteBriefTheme
import com.dailyapps.kitebrief.viewmodel.KiteViewModel

class MainActivity : ComponentActivity() {
    private val vm: KiteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KiteBriefTheme {
                KiteNav(vm)
            }
        }
    }
}
