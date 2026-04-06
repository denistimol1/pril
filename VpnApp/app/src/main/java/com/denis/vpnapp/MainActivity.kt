package com.denis.vpnapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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

    private val vlessConfig = "vless://1a6b9de8-2a15-4973-8d68-cb69cb6e62d2@185.122.171.166:19087?type=tcp&encryption=none&path=%2F&headerType=http&security=reality&pbk=XrptUvN9kt3ZOFzrKrDdqfv1G_PyhQHf8hfx-fIH_Q4&fp=chrome&sni=aws.amazon.com&sid=3679d3aa8572694e&spx=%2F"

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
                        onOpenServers = { showServers = true },
                        onConnect = { connectViaV2rayNG() },
                        onDisconnect = { disconnectV2rayNG() }
                    )
                }
            }
        }
    }

    private fun connectViaV2rayNG() {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(vlessConfig)
                setPackage("com.v2raytun.android")
            }
            startActivity(intent)
        } catch (e: Exception) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("vless", vlessConfig))
            Toast.makeText(this, "Конфиг скопирован! Вставьте в v2rayNG", Toast.LENGTH_LONG).show()
            try {
                val launchIntent = packageManager.getLaunchIntentForPackage("com.v2raytun.android")
                if (launchIntent != null) startActivity(launchIntent)
                else startActivity(Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=com.v2ray.ang")))
            } catch (ex: Exception) {
                Toast.makeText(this, "Установите v2rayNG из Play Store", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun disconnectV2rayNG() {
        try {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.v2raytun.android")
            if (launchIntent != null) startActivity(launchIntent)
        } catch (e: Exception) { }
    }
}
