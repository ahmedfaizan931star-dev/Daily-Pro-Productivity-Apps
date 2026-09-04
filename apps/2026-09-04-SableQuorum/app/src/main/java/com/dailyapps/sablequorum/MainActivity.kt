package com.dailyapps.sablequorum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.dailyapps.sablequorum.ui.QuorumRoot
import com.dailyapps.sablequorum.ui.QuorumViewModel
import com.dailyapps.sablequorum.ui.theme.SableQuorumTheme

class MainActivity : ComponentActivity() {
    private val viewModel: QuorumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SableQuorumTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    QuorumRoot(viewModel)
                }
            }
        }
    }
}
