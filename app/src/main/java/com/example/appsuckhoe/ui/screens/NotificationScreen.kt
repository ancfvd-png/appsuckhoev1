package com.example.appsuckhoe.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotificationScreen(
    onNavigateHome: () -> Unit,
    onNavigateLiveLocation: () -> Unit,
    onNavigateHistory: () -> Unit,
    onNavigateSettings: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)
    val lightBackground = Color(0xFFF8FAFC)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 16.dp, vertical = 18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Thông báo SOS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateHome,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                    label = { Text("Trang chủ") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Thông báo") },
                    label = { Text("Thông báo") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateLiveLocation,
                    icon = { Icon(Icons.Default.Place, contentDescription = "Bản đồ") },
                    label = { Text("Bản đồ") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateHistory,
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Lịch sử") },
                    label = { Text("Lịch sử") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateSettings,
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Cài đặt") },
                    label = { Text("Cài đặt") }
                )
            }
        },
        containerColor = lightBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // SOS Emergency Alert Card (Red border/tint)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFFFCDD2)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // SOS Icon badge
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFF5252)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "SOS",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Người dùng cần trợ giúp!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFFEF4444)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "08:42 - 26/09/2025",
                        fontSize = 12.sp,
                        color = Color(0xFF94A3B8)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Có thể bạn đang gặp nguy hiểm. Vui lòng kiểm tra vị trí của người dùng.",
                        fontSize = 13.sp,
                        color = Color(0xFF64748B),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onNavigateLiveLocation,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Xem vị trí",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // Section Title
            Text(
                text = "Thông báo mới nhất",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(top = 4.dp)
            )

            // History Notification List
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
                    // Item 1: Fall detection
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Accessibility, contentDescription = null, tint = Color(0xFF22C55E), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Cảnh báo té ngã", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                                Text(text = "Không có sự cố", fontSize = 12.sp, color = Color(0xFF64748B))
                            }
                        }
                        Text(text = "07:20", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Item 2: Location update
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F2FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Cập nhật vị trí", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                                Text(text = "Đang di chuyển", fontSize = 12.sp, color = Color(0xFF64748B))
                            }
                        }
                        Text(text = "06:45", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Item 3: Heart rate & SpO2
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEEEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(text = "Nhịp tim & SpO₂", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF1E293B))
                                Text(text = "Bình thường", fontSize = 12.sp, color = Color(0xFF64748B))
                            }
                        }
                        Text(text = "06:30", fontSize = 12.sp, color = Color(0xFF94A3B8))
                    }
                }
            }
        }
    }
}
