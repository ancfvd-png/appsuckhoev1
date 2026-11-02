package com.example.appsuckhoe.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    onNavigateHome: () -> Unit,
    onNavigateNotifications: () -> Unit,
    onNavigateLiveLocation: () -> Unit,
    onNavigateHistory: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)
    val lightBackground = Color(0xFFF8FAFC)

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Cài đặt",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
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
                    selected = false,
                    onClick = onNavigateNotifications,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Thông báo") },
                    label = { Text("Thông báo") }
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
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Cài đặt") },
                    label = { Text("Cài đặt") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue
                    )
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
            // Profile Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(34.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Nguyễn Văn A",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "ID: 123456",
                                color = Color(0xFF64748B),
                                fontSize = 13.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            // Settings Options Card Group
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    SettingItem(
                        icon = Icons.Default.People,
                        title = "Quản lý người thân",
                        subtitle = "Thêm/sửa số điện thoại, email",
                        onClick = {}
                    )
                    SettingItem(
                        icon = Icons.Default.NotificationsActive,
                        title = "Thiết lập cảnh báo",
                        subtitle = "SOS, té ngã, nhịp tim, nhiệt độ...",
                        onClick = {}
                    )
                    SettingItem(
                        icon = Icons.Default.Message,
                        title = "Gửi thông báo",
                        subtitle = "Điện thoại, App, Web",
                        onClick = {}
                    )
                    SettingItem(
                        icon = Icons.Default.Place,
                        title = "Vị trí & bản đồ",
                        subtitle = "Tần suất cập nhật vị trí",
                        onClick = onNavigateLiveLocation
                    )
                    SettingItem(
                        icon = Icons.Default.Language,
                        title = "Ngôn ngữ",
                        subtitle = "Tiếng Việt",
                        onClick = {}
                    )
                    SettingItem(
                        icon = Icons.Default.Info,
                        title = "Thông tin phần mềm",
                        subtitle = "Phiên bản 1.0.0",
                        onClick = {},
                        showDivider = false
                    )
                }
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF1F5F9)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color(0xFF0D3B66),
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(1.dp))
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
        if (showDivider) {
            HorizontalDivider(
                color = Color(0xFFF1F5F9),
                modifier = Modifier.padding(start = 68.dp)
            )
        }
    }
}
