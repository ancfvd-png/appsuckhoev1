package com.example.appsuckhoe.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsuckhoe.data.HealthFirebaseManager

@Composable
fun SosAlarmCallScreen(
    onDismiss: () -> Unit,
    onAccept: () -> Unit
) {
    // Gradient nền tối sang trọng
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F172A),
            Color(0xFF1E1B2E),
            Color(0xFF090D16)
        )
    )

    // Hiệu ứng mạch đập (Pulse animation) cho biểu tượng SOS
    val infiniteTransition = rememberInfiniteTransition(label = "Pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Scale"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Text(
                text = "Cuộc gọi khẩn cấp đến",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            // Red glowing pulse SOS icon
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Outer glow ring 2
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444).copy(alpha = 0.25f))
                    )
                    // Outer glow ring 1
                    Box(
                        modifier = Modifier
                            .size(130.dp)
                            .scale(scale)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444).copy(alpha = 0.45f))
                    )
                    // Red center circle
                    Box(
                        modifier = Modifier
                            .size(105.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = Color.White,
                            modifier = Modifier.size(54.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "HealthMonitorApp",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp
                )

                Text(
                    text = "CẢNH BÁO SOS",
                    color = Color.White.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Người dùng đang cần trợ giúp!\nHãy kiểm tra ngay!",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }

            // Bottom Buttons (Từ chối / Trả lời / Nhắn tin)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(28.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Nút Từ chối (Đỏ)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFEF4444))
                                .clickable {
                                    HealthFirebaseManager.updateMetric("sos_alert", false)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CallEnd,
                                contentDescription = "Từ chối",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Từ chối",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Nút Trả lời (Xanh lá)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF22C55E))
                                .clickable {
                                    onAccept()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "Trả lời",
                                tint = Color.White,
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Trả lời",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Nút Nhắn tin
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF1E293B).copy(alpha = 0.8f),
                    modifier = Modifier.clickable {
                        onDismiss()
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Message,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Nhắn tin",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
