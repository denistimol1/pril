package com.denis.vpnapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denis.vpnapp.model.VpnServer
import com.denis.vpnapp.model.VpnUiState
import com.denis.vpnapp.model.VpnViewModel
import com.denis.vpnapp.model.vpnServers
import com.denis.vpnapp.ui.theme.*

@Composable
fun ServersScreen(
    uiState: VpnUiState,
    viewModel: VpnViewModel,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Dark900, Dark800, Dark900)))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Выбор сервера",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            }

            Text(
                "  ${vpnServers.size} серверов доступно",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(vpnServers) { server ->
                    ServerItem(
                        server = server,
                        isSelected = server.id == uiState.selectedServer.id,
                        onClick = {
                            viewModel.selectServer(server)
                            onBack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ServerItem(
    server: VpnServer,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val pingColor = when {
        server.ping < 60 -> GreenOn
        server.ping < 100 -> Cyan400
        else -> TextSecondary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Dark600 else Dark700
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(
            1.5.dp, Cyan400
        ) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(server.flag, fontSize = 32.sp)
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        server.country,
                        color = TextPrimary,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            "${server.ping} мс",
                            color = pingColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            "Нагрузка ${server.load}%",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Cyan400,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
