package com.example.appsuckhoe.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsuckhoe.data.HealthDataDto

@Composable
fun HeartRateDetailScreen(
    healthData: HealthDataDto = HealthDataDto(),
    onBack: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)

    val hr = healthData.heartRate
    val sp = healthData.sp02

    val hrStatus = if (hr in 60..100) "Bình thường" else "Cần chú ý"
    val hrColor = if (hr in 60..100) Color(0xFF22C55E) else Color(0xFFEF4444)

    val spStatus = if (sp >= 95) "Bình thường" else "Thấp"
    val spColor = if (sp >= 95) Color(0xFF22C55E) else Color(0xFFEF4444)

    val dataPoints = healthData.hrHistory

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Nhịp tim & Nồng độ Oxy",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Circular Gauges Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Heart Rate Gauge
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .border(6.dp, hrColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = "$hr",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = "bpm",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Nhịp tim",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = hrStatus,
                            fontSize = 12.sp,
                            color = hrColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // SpO2 Gauge
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .border(6.dp, spColor, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "💧",
                                    fontSize = 22.sp
                                )
                                Text(
                                    text = "$sp%",
                                    fontSize = 26.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E293B)
                                )
                                Text(
                                    text = "SpO₂",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "SpO₂",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = spStatus,
                            fontSize = 12.sp,
                            color = spColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Chart Card using real ESP32 hrHistory
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Biểu đồ nhịp tim",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF1E293B)
                        )
                        Text(
                            text = "Trực tiếp từ ESP32",
                            fontSize = 12.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Line Chart with grid drawing dataPoints
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val w = size.width
                            val h = size.height

                            val lines = 4
                            for (i in 0..lines) {
                                val y = h * i / lines
                                drawLine(
                                    color = Color(0xFFE2E8F0),
                                    start = androidx.compose.ui.geometry.Offset(0f, y),
                                    end = androidx.compose.ui.geometry.Offset(w, y),
                                    strokeWidth = 1f
                                )
                            }

                            if (dataPoints.isNotEmpty()) {
                                val minVal = dataPoints.minOrNull() ?: 0f
                                val maxVal = dataPoints.maxOrNull() ?: 100f
                                val range = if (maxVal == minVal) 1f else (maxVal - minVal)

                                val points = dataPoints.mapIndexed { index, value ->
                                    val x = if (dataPoints.size > 1) w * index / (dataPoints.size - 1) else w / 2f
                                    val y = h - ((value - minVal) / range * (h * 0.7f) + h * 0.15f)
                                    androidx.compose.ui.geometry.Offset(x, y)
                                }

                                val path = Path().apply {
                                    moveTo(points[0].x, points[0].y)
                                    for (i in 1 until points.size) {
                                        val p1 = points[i - 1]
                                        val p2 = points[i]
                                        val cx = (p1.x + p2.x) / 2f
                                        cubicTo(cx, p1.y, cx, p2.y, p2.x, p2.y)
                                    }
                                }

                                drawPath(
                                    path = path,
                                    color = Color(0xFFEF4444),
                                    style = Stroke(width = 4f)
                                )

                                points.forEach { p ->
                                    drawCircle(
                                        color = Color(0xFFEF4444),
                                        radius = 6f,
                                        center = p
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("0h", "6h", "12h", "18h", "24h").forEach { time ->
                            Text(
                                text = time,
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            // Advice Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF22C55E)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Nhịp tim ($hr bpm) và nồng độ oxy trong máu ($sp%) đang được theo dõi trực tiếp từ thiết bị.",
                        fontSize = 13.sp,
                        color = Color(0xFF166534),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
