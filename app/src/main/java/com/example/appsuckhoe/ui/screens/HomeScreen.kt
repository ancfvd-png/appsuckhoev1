package com.example.appsuckhoe.ui.screens

import android.location.Geocoder
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsuckhoe.data.HealthDataDto
import java.util.Locale

@Composable
fun HomeScreen(
    healthData: HealthDataDto = HealthDataDto(),
    onNavigateToHeartRate: () -> Unit,
    onNavigateToStress: () -> Unit,
    onNavigateToFallDetection: () -> Unit,
    onNavigateToSos: () -> Unit,
    onNavigateToLiveLocation: () -> Unit,
    onNavigateToBodyTemperature: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)
    val lightBackground = Color(0xFFF8FAFC)
    val context = LocalContext.current

    var resolvedAddress by remember(healthData.latitude, healthData.longitude) {
        mutableStateOf(healthData.address)
    }

    LaunchedEffect(healthData.latitude, healthData.longitude) {
        if (healthData.latitude != 0.0 && healthData.longitude != 0.0) {
            try {
                val geocoder = Geocoder(context, Locale("vi", "VN"))
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(healthData.latitude, healthData.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    resolvedAddress = addresses[0].getAddressLine(0) ?: healthData.address
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    Scaffold(
        topBar = {
            // Top Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = Color(0xFF4ADE80)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Sức Khỏe & An Toàn",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = Color(0xFF60A5FA),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "Luôn bên bạn, mọi lúc, mọi nơi",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }

                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Bottom Navigation Bar
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                    label = { Text("Trang chủ") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = darkBlue,
                        selectedTextColor = darkBlue,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToNotifications,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Thông báo") },
                    label = { Text("Thông báo") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToLiveLocation,
                    icon = { Icon(Icons.Default.Place, contentDescription = "Bản đồ") },
                    label = { Text("Bản đồ") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHistory,
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Lịch sử") },
                    label = { Text("Lịch sử") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToSettings,
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
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE2E8F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Nguyễn Văn A",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF22C55E))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Đang hoạt động (Firebase)",
                                    color = Color(0xFF22C55E),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
            }

            // Grid Cards 2x3
            // Row 1: Nhịp tim & SpO2
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nhịp tim
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToHeartRate() }
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
                                text = "Nhịp tim",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = healthData.heartRate.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "bpm",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MiniSparkline(lineColor = Color(0xFFEF4444))
                    }
                }

                // SpO2
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToHeartRate() }
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
                                text = "SpO₂",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF0EA5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.WaterDrop,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = healthData.spO2.toString(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "%",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MiniSparkline(lineColor = Color(0xFF0EA5E9))
                    }
                }
            }

            // Row 2: Nhiệt độ cơ thể & Căng thẳng
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Nhiệt độ cơ thể
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToBodyTemperature() }
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
                                text = "Nhiệt độ cơ thể",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2563EB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Thermostat,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = healthData.bodyTemp.toString(),
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "°C",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MiniSparkline(lineColor = Color(0xFF2563EB))
                    }
                }

                // Căng thẳng
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToStress() }
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
                                text = "Căng thẳng",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF22C55E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = healthData.stressLevel,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF22C55E)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        MiniSparkline(lineColor = Color(0xFF22C55E))
                    }
                }
            }

            // Row 3: Cảnh báo té ngã & Vị trí hiện tại
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cảnh báo té ngã
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToFallDetection() }
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
                                text = "Cảnh báo té ngã",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF22C55E)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Accessibility,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (healthData.fallDetected) "Có té ngã!" else "Không có sự cố",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (healthData.fallDetected) Color(0xFFEF4444) else Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Hệ thống sẵn sàng",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Vị trí hiện tại (Đồng bộ địa chỉ thực tế từ Geocoder)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateToLiveLocation() }
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
                                text = "Vị trí hiện tại",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF3B82F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Đang di chuyển",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = resolvedAddress.take(22) + if (resolvedAddress.length > 22) "..." else "",
                            fontSize = 11.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            // SOS Emergency Button Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF5252)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToSos() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "SOS",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Bấm để gửi cảnh báo khẩn cấp",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MiniSparkline(lineColor: Color) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(0f, height * 0.7f)
            cubicTo(
                width * 0.25f, height * 0.2f,
                width * 0.5f, height * 0.9f,
                width * 0.75f, height * 0.4f
            )
            lineTo(width, height * 0.5f)
        }
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3f)
        )
    }
}
