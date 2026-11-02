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
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Thermostat
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
fun BodyTemperatureDetailScreen(
    healthData: HealthDataDto = HealthDataDto(),
    onBack: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)
    val temp = healthData.bodyTemp
    val (statusText, statusColor) = when {
        temp < 35.5 -> "Thấp" to Color(0xFF3B82F6)
        temp > 37.5 -> "Sốt" to Color(0xFFEF4444)
        else -> "Bình thường" to Color(0xFF22C55E)
    }

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
                        text = "Đo nhiệt độ cơ thể",
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
            // Temperature Gauge Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Circular Gauge
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .border(6.dp, statusColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Thermostat,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "$temp°C",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = statusText,
                                fontSize = 13.sp,
                                color = statusColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Range Bar: Thấp | Bình thường | Sốt
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color(0xFFC6F6D5), RoundedCornerShape(5.dp))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .fillMaxHeight()
                                .background(Color(0xFF22C55E), RoundedCornerShape(5.dp))
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(Color(0xFFFFEEEE), RoundedCornerShape(5.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(text = "Thấp", fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.SemiBold)
                            Text(text = "< 35.5", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Bình thường", fontSize = 12.sp, color = Color(0xFF22C55E), fontWeight = FontWeight.Bold)
                            Text(text = "35.5 - 37.5", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Sốt", fontSize = 12.sp, color = Color(0xFFEF4444), fontWeight = FontWeight.Bold)
                            Text(text = "> 37.5", fontSize = 11.sp, color = Color(0xFF94A3B8))
                        }
                    }
                }
            }

            // Chart Card
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
                            text = "Biểu đồ nhiệt độ",
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

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
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

                            val path = Path().apply {
                                moveTo(0f, h * 0.6f)
                                cubicTo(w * 0.25f, h * 0.55f, w * 0.5f, h * 0.65f, w * 0.75f, h * 0.5f)
                                cubicTo(w * 0.9f, h * 0.6f, w * 0.95f, h * 0.58f, w, h * 0.6f)
                            }

                            drawPath(
                                path = path,
                                color = statusColor,
                                style = Stroke(width = 3f)
                            )
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
                colors = CardDefaults.cardColors(containerColor = if (temp > 37.5) Color(0xFFFFEEEE) else Color(0xFFF0FDF4)),
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
                            .background(statusColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = when (statusText) {
                            "Sốt" -> "Nhiệt độ cơ thể quá 37.5°C ($temp°C - Sốt!). Hãy nghỉ ngơi, uống đủ nước và theo dõi sát sao!"
                            "Thấp" -> "Nhiệt độ cơ thể đang thấp ($temp°C). Hãy giữ ấm cơ thể!"
                            else -> "Nhiệt độ cơ thể của bạn đang ở mức bình thường ($temp°C)."
                        },
                        fontSize = 13.sp,
                        color = if (temp > 37.5) Color(0xFF991B1B) else Color(0xFF166534),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
