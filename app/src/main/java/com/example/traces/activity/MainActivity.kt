package com.example.traces.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.traces.ui.theme.*
import kotlinx.coroutines.launch
import me.raghu.opentelsdk.EventCollector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ComposeMVVMWeatherAppTheme {
                TwoButtonComposable()
            }
        }

    }

    @Composable
    fun TwoButtonComposable() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Button(onClick = {
                lifecycleScope.launch() {
                    EventCollector.handleEvent("Go Shopping", "")
                }
            }) {
                Text(text = "Go Shopping")
            }
            Spacer(modifier = Modifier.height(16.dp))  // Adds space between the buttons
            Button(onClick = {
                lifecycleScope.launch() {
                    EventCollector.handleEvent("Go Shopping", "Add to cart")
                }
            }) {
                Text(text = "Add to cart")
            }
        }
    }


}
