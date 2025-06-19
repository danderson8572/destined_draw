package com.mtg.randomcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mtg.randomcard.ui.AppNavGraph
import com.mtg.randomcard.viewmodel.RandomCardViewModel
import com.mtg.randomcard.ui.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: RandomCardViewModel = viewModel()
            AppTheme(vm) { AppNavGraph(sharedVm = vm) }
        }
    }
}
