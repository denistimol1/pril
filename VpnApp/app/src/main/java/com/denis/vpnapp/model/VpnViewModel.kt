package com.denis.vpnapp.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

enum class VpnStatus { DISCONNECTED, CONNECTING, CONNECTED }

data class VpnUiState(
    val status: VpnStatus = VpnStatus.DISCONNECTED,
    val selectedServer: VpnServer = vpnServers[0],
    val connectedSeconds: Long = 0L,
    val downloadSpeed: String = "0 KB/s",
    val uploadSpeed: String = "0 KB/s",
    val currentIp: String = "—"
)

class VpnViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(VpnUiState())
    val uiState: StateFlow<VpnUiState> = _uiState

    private var timerJob: Job? = null
    private var speedJob: Job? = null

    fun selectServer(server: VpnServer) {
        if (_uiState.value.status == VpnStatus.DISCONNECTED) {
            _uiState.value = _uiState.value.copy(selectedServer = server)
        }
    }

    fun toggleConnection() {
        when (_uiState.value.status) {
            VpnStatus.DISCONNECTED -> connect()
            VpnStatus.CONNECTING -> disconnect()
            VpnStatus.CONNECTED -> disconnect()
        }
    }

    private fun connect() {
        _uiState.value = _uiState.value.copy(status = VpnStatus.CONNECTING)
        viewModelScope.launch {
            delay(2500)
            _uiState.value = _uiState.value.copy(
                status = VpnStatus.CONNECTED,
                currentIp = _uiState.value.selectedServer.host,
                connectedSeconds = 0L
            )
            startTimer()
            startSpeedSimulation()
        }
    }

    private fun disconnect() {
        timerJob?.cancel()
        speedJob?.cancel()
        _uiState.value = _uiState.value.copy(
            status = VpnStatus.DISCONNECTED,
            connectedSeconds = 0L,
            downloadSpeed = "0 KB/s",
            uploadSpeed = "0 KB/s",
            currentIp = "—"
        )
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                _uiState.value = _uiState.value.copy(
                    connectedSeconds = _uiState.value.connectedSeconds + 1
                )
            }
        }
    }

    private fun startSpeedSimulation() {
        speedJob = viewModelScope.launch {
            val speeds = listOf("1.2 MB/s", "856 KB/s", "2.1 MB/s", "1.5 MB/s", "980 KB/s", "1.8 MB/s")
            val upl = listOf("256 KB/s", "128 KB/s", "512 KB/s", "384 KB/s", "200 KB/s")
            var i = 0
            while (true) {
                delay(2000)
                _uiState.value = _uiState.value.copy(
                    downloadSpeed = speeds[i % speeds.size],
                    uploadSpeed = upl[i % upl.size]
                )
                i++
            }
        }
    }

    fun formatTime(seconds: Long): String {
        val h = seconds / 3600
        val m = (seconds % 3600) / 60
        val s = seconds % 60
        return "%02d:%02d:%02d".format(h, m, s)
    }
}
