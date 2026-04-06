package com.denis.vpnapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denis.vpnapp.model.VpnStatus
import com.denis.vpnapp.model.VpnUiState
import com.denis.vpnapp.model.VpnViewModel
import com.denis.vpnapp.ui.theme.*

@Composable
fun MainScreen(
    uiState: VpnUiState,
    viewModel: VpnViewModel,
    onOpenServers: () -> Unit,
    onConnect: () -> Unit,
    onDisconnect: () -> Unit
) {
    val status = uiState.status

    val buttonColor by animateColorAsState(
        targetValue = when (status) {
            VpnStatus.DISCONNECTED -> Dark600
            VpnStatus.CONNECTING -> Color(0xFF1A3A5C)
            VpnStatus.CONNECTED -> Color(0xFF003D1A)
        },
        animationSpec = tween(600), label = "buttonColor"
    )

    val ringColor by animateColorAsState(
        targetValue = when (status) {
            VpnStatus.DISCONNECTED -> TextSecondary
            VpnStatus.CONNECTING -> Cyan400
            VpnStatus.CONNECTED -> GreenOn
        },
        animationSpec = tween(600), label = "ringColor"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (status == VpnStatus.CONNECTING) 1.12f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Dark900, Dark800, Dark900))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("SafeVPN", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Cyan400)
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = TextSecondary, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = when (status) {
                    VpnStatus.DISCONNECTED -> "Не подключено"
                    VpnStatus.CONNECTING -> "Подключение..."
                    VpnStatus.CONNECTED -> "Защищено"
                },
                fontSize = 16.sp,
                color = when (status) {
                    VpnStatus.DISCONNECTED -> TextSecondary
                    VpnStatus.CONNECTING -> Cyan400
                    VpnStatus.CONNECTED -> GreenOn
                },
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (status == VpnStatus.CONNECTED)
                    viewModel.formatTime(uiState.connectedSeconds)


,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(40.dp))

            Box(contentAlignment = Alignment.Center, modifier = Modifier.scale(pulse)) {
                Box(modifier = Modifier.size(180.dp).clip(CircleShape).border(2.dp, ringColor.copy(alpha = 0.3f), CircleShape))
                Box(modifier = Modifier.size(156.dp).clip(CircleShape).border(1.5.dp, ringColor.copy(alpha = 0.6f), CircleShape))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(buttonColor)
                        .border(2.dp, ringColor, CircleShape)
                        .clickable {
                            if (status == VpnStatus.DISCONNECTED) {
                                viewModel.toggleConnection()
                                onConnect()
                            } else {
                                viewModel.toggleConnection()
                                onDisconnect()
                            }
                        }
                ) {
                    Icon(
                        imageVector = if (status == VpnStatus.CONNECTED) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = "Connect",
                        tint = ringColor,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Card(
                modifier = Modifier.fillMaxWidth().clickable { onOpenServers() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Dark700)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(uiState.selectedServer.flag, fontSize = 28.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(uiState.selectedServer.country, color = TextPrimary, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                            Text("${uiState.selectedServer.ping} мс • ${uiState.selectedServer.load}% нагрузка", color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                    Icon(Icons.Default.KeyboardArrowRight, contentDescription = null, tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (status == VpnStatus.CONNECTED) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    SpeedCard(modifier = Modifier.weight(1f), label = "Загрузка", value = uiState.downloadSpeed, icon = Icons.Default.ArrowDownward, color = Cyan400)
                    SpeedCard(modifier = Modifier.weight(1f), label = "Отдача", value = uiState.uploadSpeed, icon = Icons.Default.ArrowUpward, color = GreenOn)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Dark700)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        vertical


Alignment = Alignment.CenterVertically
                    ) {
                        Text("Ваш IP", color = TextSecondary, fontSize = 13.sp)
                        Text(uiState.currentIp, color = Cyan400, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SpeedCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Dark700)
    ) {
        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = TextPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(label, color = TextSecondary, fontSize = 11.sp)
        }
    }
}




