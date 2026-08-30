package com.dailyapps.quilldeck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.dailyapps.quilldeck.ui.navigation.QuillNav
import com.dailyapps.quilldeck.ui.theme.QuillDeckTheme
import com.dailyapps.quilldeck.viewmodel.QuillViewModel

class MainActivity : ComponentActivity() {
    private val vm: QuillViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuillDeckTheme {
                QuillNav(vm)
            }
        }
    }
}
