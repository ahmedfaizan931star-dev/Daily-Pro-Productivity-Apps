package com.dailyapps.ridgenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.dailyapps.ridgenote.ui.navigation.RidgeNav
import com.dailyapps.ridgenote.ui.theme.RidgeNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RidgeNoteTheme { RidgeNav() }
        }
    }
}
