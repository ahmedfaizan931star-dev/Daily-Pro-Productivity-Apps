package com.dailyapps.solsticeflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dailyapps.solsticeflow.ui.navigation.SolsticeNavGraph
import com.dailyapps.solsticeflow.ui.theme.SolsticeFlowTheme
import com.dailyapps.solsticeflow.viewmodel.SolsticeViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SolsticeFlowTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: SolsticeViewModel = viewModel()
                    SolsticeNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
