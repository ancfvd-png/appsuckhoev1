package com.example.appsuckhoe.ui.screens

import android.location.Geocoder
import android.preference.PreferenceManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.appsuckhoe.data.HealthDataDto
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.Locale

@Composable
fun LiveLocationDetailScreen(
    healthData: HealthDataDto = HealthDataDto(),
    onBack: () -> Unit
) {
    val darkBlue = Color(0xFF0D3B66)
    val context = LocalContext.current

    // Cấu hình User-Agent duy nhất cho OSMDroid
    remember {
        @Suppress("DEPRECATION")
        Configuration.getInstance().load(context, PreferenceManager.getDefaultSharedPreferences(context))
        Configuration.getInstance().userAgentValue = "DoAnIoT_AnNguyen_UNETI"
        true
    }

    val lat = if (healthData.latitude != 0.0) healthData.latitude else 21.018400
    val lon = if (healthData.longitude != 0.0) healthData.longitude else 105.846100
    val currentGeoPoint = GeoPoint(lat, lon)

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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBlue)
                    .padding(horizontal = 16.dp, vertical = 14.dp)
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
                        text = "Vị trí trực tiếp",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        },
        containerColor = Color(0xFFE2E8F0)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Bản đồ OSMDroid lớn chiếm toàn bộ màn hình
            val hotTileSource = remember {
                XYTileSource(
                    "OSM_HOT",
                    0, 19, 256, ".png",
                    arrayOf(
                        "https://a.tile.openstreetmap.fr/hot/",
                        "https://b.tile.openstreetmap.fr/hot/",
                        "https://c.tile.openstreetmap.fr/hot/"
                    ),
                    "© OpenStreetMap contributors, Tiles courtesy of Humanitarian OpenStreetMap Team"
                )
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { ctx ->
                    MapView(ctx).apply {
                        setTileSource(hotTileSource)
                        setMultiTouchControls(true)
                        controller.setZoom(17.0)
                        controller.setCenter(currentGeoPoint)
                    }
                },
                update = { mapView ->
                    mapView.overlays.clear()
                    val marker = Marker(mapView).apply {
                        position = currentGeoPoint
                        title = "Vị trí người bệnh"
                        snippet = resolvedAddress
                    }
                    mapView.overlays.add(marker)
                    mapView.controller.animateTo(currentGeoPoint)
                    mapView.invalidate()
                }
            )

            // Card thông tin thu nhỏ gọn ở dưới cùng để ưu tiên diện tích bản đồ
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tọa độ: ${healthData.latitude}, ${healthData.longitude}",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF1E293B)
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    tint = Color(0xFF22C55E),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Live ESP32",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF166534)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFFF1F5F9))

                        Text(
                            text = resolvedAddress,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
