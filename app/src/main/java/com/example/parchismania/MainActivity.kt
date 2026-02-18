package com.example.parchismania

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.parchismania.ui.AppNav
import com.example.parchismania.ui.theme.ParchisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ParchisTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val vm: GameViewModel = viewModel()
                    AppNav(vm = vm)
                }
            }
        }
    }
}
