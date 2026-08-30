package com.dailyapps.ledgermesa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.ledgermesa.ui.navigation.LedgerNav
import com.dailyapps.ledgermesa.ui.theme.LedgerTheme
import com.dailyapps.ledgermesa.viewmodel.LedgerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LedgerTheme {
                val vm: LedgerViewModel = viewModel()
                LedgerNav(vm)
            }
        }
    }
}
