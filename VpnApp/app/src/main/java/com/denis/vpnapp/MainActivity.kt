package com.denis.vpnapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import com.denis.vpnapp.model.VpnViewModel
import com.denis.vpnapp.ui.screens.MainScreen
import com.denis.vpnapp.ui.screens.ServersScreen
import com.denis.vpnapp.ui.theme.VpnAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: VpnViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VpnAppTheme {
                val uiState by viewModel.uiState.collectAsState()
                var showServers by remember { mutableStateOf(false) }

                if (showServers) {
                    ServersScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onBack = { showServers = false }
                    )
                } else {
                    MainScreen(
                        uiState = uiState,
                        viewModel = viewModel,
                        onOpenServers = { showServers = true }
                    )
                }
            }
        }
    }
}
