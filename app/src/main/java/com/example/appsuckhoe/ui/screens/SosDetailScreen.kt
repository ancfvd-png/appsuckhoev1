package com.example.appsuckhoe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsuckhoe.data.HealthDataDto
import com.example.appsuckhoe.data.HealthFirebaseManager

@Composable
fun SosDetailScreen(
    healthData: HealthDataDto = HealthDataDto(),
    onBack: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)
    val isSosActive = healthData.sos_alert

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
                        text = "Cảnh báo nguy hiểm (SOS)",
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Big SOS Button with concentric circles
            Box(
                modifier = Modifier
                    .size(190.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer ring 3
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .clip(CircleShape)
                        .background((if (isSosActive) Color(0xFFFF5252) else Color(0xFFFFCDD2)).copy(alpha = 0.5f))
                )
                // Outer ring 2
                Box(
                    modifier = Modifier
                        .size(155.dp)
                        .clip(CircleShape)
                        .background((if (isSosActive) Color(0xFFEF5350) else Color(0xFFEF5350)).copy(alpha = 0.4f))
                )
                // Outer ring 1
                Box(
                    modifier = Modifier
                        .size(125.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE53935).copy(alpha = 0.7f))
                )
                // Core SOS Button
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(if (isSosActive) Color(0xFFB91C1C) else Color(0xFFFF5252))
                        .clickable {
                            // Gửi tín hiệu SOS_ALERT đồng thời tới ESP32 qua Firebase
                            HealthFirebaseManager.updateMetric("sos_alert", !isSosActive)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            text = if (isSosActive) "BÁO ĐỘNG!" else "SOS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = if (isSosActive) 14.sp else 24.sp
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isSosActive) "ĐANG BẬT BÁO ĐỘNG SOS!" else "Nhấn để kích hoạt cảnh báo",
                    color = Color(0xFFE53935),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Chế độ đồng thời gửi tín hiệu tới ESP32 & Người thân",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
            }

            // Notification Targets Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Gửi thông báo đồng thời đến",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        // Phone
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = Color(0xFF0284C7),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Điện thoại",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "(người thân)",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        // App / ESP32
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Apps,
                                    contentDescription = null,
                                    tint = Color(0xFF0284C7),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "ESP32",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "(phần cứng)",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }

                        // Web
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFFF3E8FF)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = null,
                                    tint = Color(0xFF9333EA),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Web",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E293B)
                            )
                            Text(
                                text = "(trang quản lý)",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }

            // Info Notice Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSosActive) Color(0xFFFFEEEE) else Color(0xFFFFF5F5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFEF4444),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (isSosActive) "Cảnh báo SOS đang BẬT! ESP32 và ứng dụng đang kích hoạt báo động đồng thời." else "Khi kích hoạt, hệ thống sẽ gửi thông báo SOS đồng thời tới ESP32 và các kênh đã cài đặt.",
                        fontSize = 12.sp,
                        color = Color(0xFFB91C1C),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
