package com.denis.vpnapp.model

data class VpnServer(
    val id: Int,
    val name: String,
    val country: String,
    val flag: String,
    val host: String,
    val ping: Int,
    val load: Int
)

val vpnServers = listOf(
    VpnServer(1, "Netherlands", "Нидерланды", "🇳🇱", "185.122.171.166", 45, 23),
    VpnServer(2, "Germany", "Германия", "🇩🇪", "185.122.171.166", 62, 41),
    VpnServer(3, "Finland", "Финляндия", "🇫🇮", "185.122.171.166", 58, 15),
    VpnServer(4, "France", "Франция", "🇫🇷", "185.122.171.166", 71, 33),
    VpnServer(5, "USA", "США", "🇺🇸", "185.122.171.166", 120, 55),
)
