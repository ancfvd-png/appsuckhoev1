package com.example.appsuckhoe

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.appsuckhoe.data.HealthFirebaseManager
import com.example.appsuckhoe.data.SosMonitor
import com.example.appsuckhoe.ui.screens.BodyTemperatureDetailScreen
import com.example.appsuckhoe.ui.screens.FallDetectionDetailScreen
import com.example.appsuckhoe.ui.screens.HeartRateDetailScreen
import com.example.appsuckhoe.ui.screens.HistoryScreen
import com.example.appsuckhoe.ui.screens.HomeScreen
import com.example.appsuckhoe.ui.screens.LiveLocationDetailScreen
import com.example.appsuckhoe.ui.screens.NotificationScreen
import com.example.appsuckhoe.ui.screens.SettingsScreen
import com.example.appsuckhoe.ui.screens.SosAlarmCallScreen
import com.example.appsuckhoe.ui.screens.SosDetailScreen
import com.example.appsuckhoe.ui.screens.StressDetailScreen
import com.example.appsuckhoe.ui.theme.AppSucKhoeTheme
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private var sosMonitor: SosMonitor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createSOSNotificationChannel()
        requestNotificationPermission()
        registerFCMToken()

        // Khởi động SosMonitor theo dõi thời gian thực sự kiện SOS từ Firebase
        sosMonitor = SosMonitor(this).apply { start() }

        setContent {
            AppSucKhoeTheme {
                val healthData by HealthFirebaseManager.healthData.collectAsState()
                var currentScreen by remember { mutableStateOf("home") }

                if (healthData.sos_alert) {
                    // Hiển thị màn hình cuộc gọi cảnh báo khẩn cấp toàn màn hình
                    SosAlarmCallScreen(
                        onDismiss = {
                            HealthFirebaseManager.updateMetric("sos_alert", false)
                        },
                        onAccept = {
                            currentScreen = "live_location"
                        }
                    )
                } else {
                    when (currentScreen) {
                        "home" -> HomeScreen(
                            healthData = healthData,
                            onNavigateToHeartRate = { currentScreen = "heart_rate" },
                            onNavigateToStress = { currentScreen = "stress" },
                            onNavigateToFallDetection = { currentScreen = "fall_detection" },
                            onNavigateToSos = { currentScreen = "sos" },
                            onNavigateToLiveLocation = { currentScreen = "live_location" },
                            onNavigateToBodyTemperature = { currentScreen = "body_temperature" },
                            onNavigateToNotifications = { currentScreen = "notifications" },
                            onNavigateToHistory = { currentScreen = "history" },
                            onNavigateToSettings = { currentScreen = "settings" }
                        )
                        "heart_rate" -> HeartRateDetailScreen(
                            healthData = healthData,
                            onBack = { currentScreen = "home" }
                        )
                        "stress" -> StressDetailScreen(
                            onBack = { currentScreen = "home" }
                        )
                        "fall_detection" -> FallDetectionDetailScreen(
                            onBack = { currentScreen = "home" }
                        )
                        "sos" -> SosDetailScreen(
                            healthData = healthData,
                            onBack = { currentScreen = "home" }
                        )
                        "live_location" -> LiveLocationDetailScreen(
                            healthData = healthData,
                            onBack = { currentScreen = "home" }
                        )
                        "body_temperature" -> BodyTemperatureDetailScreen(
                            healthData = healthData,
                            onBack = { currentScreen = "home" }
                        )
                        "notifications" -> NotificationScreen(
                            onNavigateHome = { currentScreen = "home" },
                            onNavigateLiveLocation = { currentScreen = "live_location" },
                            onNavigateHistory = { currentScreen = "history" },
                            onNavigateSettings = { currentScreen = "settings" }
                        )
                        "history" -> HistoryScreen(
                            healthData = healthData,
                            onNavigateHome = { currentScreen = "home" },
                            onNavigateNotifications = { currentScreen = "notifications" },
                            onNavigateLiveLocation = { currentScreen = "live_location" },
                            onNavigateHistory = { currentScreen = "history" },
                            onNavigateSettings = { currentScreen = "settings" }
                        )
                        "settings" -> SettingsScreen(
                            onNavigateHome = { currentScreen = "home" },
                            onNavigateNotifications = { currentScreen = "notifications" },
                            onNavigateLiveLocation = { currentScreen = "live_location" },
                            onNavigateHistory = { currentScreen = "history" }
                        )
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        sosMonitor?.stop()
        super.onDestroy()
    }

    private fun createSOSNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "sos_alerts",
                "Cảnh báo khẩn cấp SOS",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Thông báo khi người dùng kích hoạt nút SOS khẩn cấp"
                enableVibration(true)
                setShowBadge(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    private fun registerFCMToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM", "Không lấy được token FCM", task.exception)
                    return@addOnCompleteListener
                }

                val token = task.result
                Log.d("FCM_TOKEN", "FCM Token: $token")

                // Lưu FCM token lên Firebase Realtime Database tại Users/user_123/fcmToken
                FirebaseDatabase.getInstance("https://healthmonitorapp-bb874-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Users/user_123/fcmToken")
                    .setValue(token)
                    .addOnSuccessListener {
                        Log.d("FCM", "Đã lưu token thành công lên Firebase!")
                    }
                    .addOnFailureListener { e ->
                        Log.e("FCM", "Lỗi khi lưu token lên Firebase", e)
                    }
            }
    }
}
