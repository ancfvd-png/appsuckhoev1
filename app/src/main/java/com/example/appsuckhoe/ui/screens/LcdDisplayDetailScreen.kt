package com.example.appsuckhoe.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LcdDisplayDetailScreen(
    onBack: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)

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
                        text = "Hiển thị thông số qua LCD",
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
            // Simulated LCD Hardware Screen
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF111827))
                            .border(4.dp, Color(0xFF374151), RoundedCornerShape(12.dp))
                            .padding(20.dp)
                    ) {
                        // Battery icon top right
                        Row(
                            modifier = Modifier.align(Alignment.TopEnd),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.BatteryFull,
                                contentDescription = null,
                                tint = Color(0xFF00F0FF),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // LCD Content
                        Column(
                            modifier = Modifier.align(Alignment.CenterStart),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "HR:  72 bpm",
                                color = Color(0xFF00F0FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "SpO₂: 98%",
                                color = Color(0xFF00F0FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "Temp: 36.5°C",
                                color = Color(0xFF00F0FF),
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Description Header
            Text(
                text = "Các thông số hiển thị trên LCD",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1E293B)
            )

            // Parameters List Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Item 1: Heart rate
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEEEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Nhịp tim (bpm)", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                    }

                    // Item 2: SpO2
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE0F2FE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.WaterDrop, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Nồng độ Oxy trong máu (SpO₂)", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                    }

                    // Item 3: Temp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFFEEEE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Thermostat, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Nhiệt độ cơ thể (°C)", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                    }

                    // Item 4: Stress
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Spa, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Căng thẳng (Low/Medium/High)", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                    }

                    // Item 5: Location GPS
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF3E8FF)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF9333EA), modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Vị trí (GPS)", fontWeight = FontWeight.Medium, fontSize = 15.sp, color = Color(0xFF1E293B))
                    }
                }
            }

            // Footer Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
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
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "LCD hiển thị nhanh các thông số cơ bản ngay trên thiết bị.",
                        fontSize = 13.sp,
                        color = Color(0xFF0369A1),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
