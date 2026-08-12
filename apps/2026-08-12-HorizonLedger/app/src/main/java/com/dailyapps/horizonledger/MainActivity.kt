package com.dailyapps.horizonledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.horizonledger.ui.navigation.HorizonNavGraph
import com.dailyapps.horizonledger.ui.theme.HorizonLedgerTheme
import com.dailyapps.horizonledger.viewmodel.LedgerViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceedInstanceState)
        enableEdgeToEdge()
        setContent {
            HorizonLedgerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: LedgerViewModel = viewModel()
                    HorizonNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
