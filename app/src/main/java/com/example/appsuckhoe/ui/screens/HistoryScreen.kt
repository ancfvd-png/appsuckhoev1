package com.example.appsuckhoe.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsuckhoe.data.HealthDataDto
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

private val PageBg = Color(0xFFF7F7F7)
private val HeartRed = Color(0xFFF05B63)
private val Muted = Color(0xFF929292)
private val TextMain = Color(0xFF171717)

@Composable
fun HistoryScreen(
    healthData: HealthDataDto = HealthDataDto(),
    onNavigateHome: () -> Unit,
    onNavigateNotifications: () -> Unit,
    onNavigateLiveLocation: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    var period by remember { mutableIntStateOf(0) } // 0 latest/day, 1 week, 2 month
    val hr = healthData.hrHistory.filter { it > 0f }
    val currentHr = healthData.heartRate.takeIf { it > 0 }
    val values = when (period) {
        0 -> hr.takeLast(24)
        1 -> hr.takeLast(7)
        else -> hr
    }
    val average = values.takeIf { it.isNotEmpty() }?.average()?.roundToInt()
    val min = values.minOrNull()?.roundToInt()
    val max = values.maxOrNull()?.roundToInt()
    val dateText = remember(period) {
        val now = Date()
        when (period) {
            0 -> SimpleDateFormat("dd/MM/yyyy", Locale("vi", "VN")).format(now)
            1 -> "7 mẫu gần nhất"
            else -> SimpleDateFormat("MM/yyyy", Locale("vi", "VN")).format(now)
        }
    }
    val titlePeriod = when (period) {
        0 -> "Tổng kết gần nhất"
        1 -> "Tổng kết tuần"
        else -> "Tổng kết tháng"
    }

    Scaffold(
        containerColor = PageBg,
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth().background(PageBg)
                    .padding(start = 20.dp, end = 16.dp, top = 10.dp, bottom = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onNavigateHome) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Quay lại", tint = TextMain)
                    }
                    Spacer(Modifier.weight(1f))
                    Text("Lịch sử sức khỏe", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = TextMain)
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Tùy chọn", tint = TextMain)
                    }
                }
                Text(dateText, modifier = Modifier.align(Alignment.CenterHorizontally),
                    color = Color.DarkGray, fontSize = 14.sp)
            }
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 5.dp) {
                NavigationBarItem(false, onNavigateHome, icon = { Icon(Icons.Default.Home, null) }, label = { Text("Trang chủ") })
                NavigationBarItem(false, onNavigateNotifications, icon = { Icon(Icons.Default.Notifications, null) }, label = { Text("Thông báo") })
                NavigationBarItem(false, onNavigateLiveLocation, icon = { Icon(Icons.Default.Place, null) }, label = { Text("Bản đồ") })
                NavigationBarItem(true, onNavigateHistory, icon = { Icon(Icons.Default.History, null) }, label = { Text("Lịch sử") })
                NavigationBarItem(false, onNavigateSettings, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("Cài đặt") })
            }
        }
    ) { padding ->
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color(0xFFECECEC)).padding(4.dp)
            ) {
                listOf("Mới nhất", "Tuần", "Tháng").forEachIndexed { index, label ->
                    Box(
                        Modifier.weight(1f).clip(RoundedCornerShape(15.dp))
                            .background(if (period == index) Color.White else Color.Transparent)
                            .clickable { period = index }.padding(vertical = 11.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, color = TextMain, fontSize = 14.sp,
                            fontWeight = if (period == index) FontWeight.SemiBold else FontWeight.Normal)
                    }
                }
            }

            Column(Modifier.fillMaxWidth()) {
                Text(currentHr?.toString() ?: "--", fontSize = 38.sp, fontWeight = FontWeight.Medium, color = Color.Black)
                Text("BPM", modifier = Modifier.offset(y = (-5).dp), fontSize = 14.sp, color = Color.DarkGray)
                Text(dateText, color = Muted, fontSize = 14.sp)
            }

            HeartRateChart(values = values, period = period)

            WhitePanel {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(38.dp).clip(CircleShape).background(HeartRed), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.RemoveRedEye, null, tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text(titlePeriod, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
                }
                HorizontalDivider(color = Color(0xFFEAEAEA), modifier = Modifier.padding(vertical = 14.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    StatValue("${min ?: "--"}-${max ?: "--"}", "BPM", "Phạm vi nhịp tim")
                    StatValue("${average ?: "--"}", "BPM", "Trung bình")
                }
                Spacer(Modifier.height(20.dp))
                StatValue("--", "BPM", "Nhịp tim nghỉ ngơi")
                Text("Chưa có dữ liệu đo lúc nghỉ ngơi riêng biệt", color = Muted, fontSize = 12.sp)
            }

            WhitePanel {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(38.dp).clip(CircleShape).background(HeartRed), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.DonutLarge, null, tint = Color.White)
                    }
                    Spacer(Modifier.width(12.dp))
                    Text("Phân vùng nhịp tim", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
                }
                HorizontalDivider(color = Color(0xFFEAEAEA), modifier = Modifier.padding(vertical = 14.dp))
                // Chỉ phân loại mẫu đo theo vùng đơn giản; không quy đổi thành thời lượng vận động.
                val light = values.count { it in 1f..100f }
                val elevated = values.count { it > 100f }
                val total = values.size.coerceAtLeast(1)
                Row(Modifier.fillMaxWidth().height(14.dp).clip(CircleShape).background(Color(0xFFECECEC))) {
                    if (light > 0) Box(Modifier.weight(light.toFloat()).fillMaxHeight().background(Color(0xFF45B5EA)))
                    if (elevated > 0) Box(Modifier.weight(elevated.toFloat()).fillMaxHeight().background(Color(0xFFFF9A35)))
                    if (light + elevated < total) Spacer(Modifier.weight((total - light - elevated).toFloat()))
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    ZoneValue(Color(0xFF45B5EA), "$light mẫu", "≤ 100 BPM")
                    ZoneValue(Color(0xFFFF9A35), "$elevated mẫu", "> 100 BPM")
                }
                Spacer(Modifier.height(8.dp))
                Text("Đây là số mẫu đo, không phải số phút vận động trong vùng nhịp tim.", color = Muted, fontSize = 11.sp)
            }

            WhitePanel {
                Text("Chỉ số sức khỏe khác", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
                HorizontalDivider(color = Color(0xFFEAEAEA), modifier = Modifier.padding(vertical = 12.dp))
                MiniMetric(Icons.Default.WaterDrop, "SpO₂", "${healthData.sp02}%", Color(0xFF399BE8))
                MiniMetric(Icons.Default.Thermostat, "Nhiệt độ", "${"%.1f".format(Locale.US, healthData.bodyTemp)} °C", Color(0xFFF39A42))
                MiniMetric(Icons.Default.Spa, "Căng thẳng", healthData.stressLevel, Color(0xFF43B581))
            }

            WhitePanel {
                Text("Sự kiện sức khỏe", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextMain)
                HorizontalDivider(color = Color(0xFFEAEAEA), modifier = Modifier.padding(vertical = 12.dp))
                EventLine("Té ngã", if (healthData.fallDetected) "Phát hiện té ngã" else "Không phát hiện", healthData.fallDetected)
                EventLine("SOS", if (healthData.sos_alert) "Đang có cảnh báo SOS" else "Không có cảnh báo", healthData.sos_alert)
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun HeartRateChart(values: List<Float>, period: Int) {
    WhitePanel {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Favorite, null, tint = HeartRed, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("Nhịp tim", fontSize = 17.sp, fontWeight = FontWeight.Bold, color = TextMain)
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            listOf("140", "112", "84", "56", "28", "0").forEach { Text(it, color = Muted, fontSize = 10.sp) }
        }
        Canvas(Modifier.fillMaxWidth().height(205.dp)) {
            val left = 2f
            val right = size.width - 2f
            val top = 4f
            val bottom = size.height - 4f
            for (i in 0..4) {
                val y = top + (bottom - top) * i / 4f
                drawLine(Color(0xFFE2E2E2), Offset(left, y), Offset(right, y), 1.dp.toPx())
            }
            if (values.isNotEmpty()) {
                val minY = 0f
                val maxY = 140f
                val points = values.mapIndexed { index, v ->
                    val x = if (values.size == 1) size.width / 2f else left + (right-left) * index / (values.size-1)
                    val y = bottom - ((v.coerceIn(minY, maxY)-minY)/(maxY-minY))*(bottom-top)
                    Offset(x,y)
                }
                if (points.size > 1) {
                    val path = Path().apply { moveTo(points.first().x, points.first().y); points.drop(1).forEach { lineTo(it.x,it.y) } }
                    drawPath(path, HeartRed, style = Stroke(width = 3.dp.toPx()))
                }
                points.forEach { drawCircle(HeartRed, radius = 3.5.dp.toPx(), center = it) }
            }
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            val labels = when(period) {
                0 -> listOf("Mẫu cũ", "Gần đây")
                1 -> listOf("7 mẫu gần nhất")
                else -> listOf("Đầu danh sách", "Mới nhất")
            }
            labels.forEach { Text(it, color = Muted, fontSize = 11.sp) }
        }
        if (values.isEmpty()) {
            Text("Chưa có dữ liệu nhịp tim trong khoảng này.", color = Muted, fontSize = 12.sp)
        }
    }
}

@Composable
private fun WhitePanel(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(18.dp), content = content)
    }
}

@Composable
private fun StatValue(value: String, unit: String, label: String) {
    Column(Modifier.widthIn(min = 110.dp)) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 25.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            Spacer(Modifier.width(4.dp))
            Text(unit, fontSize = 12.sp, color = Muted)
        }
        Spacer(Modifier.height(5.dp))
        Text(label, fontSize = 12.sp, color = Muted)
    }
}

@Composable
private fun ZoneValue(color: Color, amount: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(9.dp).clip(RoundedCornerShape(3.dp)).background(color))
        Spacer(Modifier.width(7.dp))
        Column {
            Text(amount, fontSize = 19.sp, color = Color.Black)
            Text(label, fontSize = 11.sp, color = Muted)
        }
    }
}

@Composable
private fun MiniMetric(icon: ImageVector, label: String, value: String, color: Color) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = color, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(12.dp))
        Text(label, modifier = Modifier.weight(1f), color = TextMain, fontSize = 14.sp)
        Text(value, color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun EventLine(title: String, status: String, alert: Boolean) {
    Row(Modifier.fillMaxWidth().padding(vertical = 7.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(if (alert) Icons.Default.Warning else Icons.Default.CheckCircle,
            null, tint = if (alert) HeartRed else Color(0xFF36A66A))
        Spacer(Modifier.width(10.dp))
        Text(title, modifier = Modifier.weight(1f), color = TextMain)
        Text(status, color = if (alert) HeartRed else Color(0xFF36A66A), fontSize = 12.sp)
    }
}
