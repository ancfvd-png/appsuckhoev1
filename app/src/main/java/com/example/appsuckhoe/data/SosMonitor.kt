package com.example.appsuckhoe.data

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SosMonitor(private val context: Context) {

    private val CHANNEL_ID = "sos_alerts"
    private val NOTIFICATION_ID = 1001
    private var sosRef: DatabaseReference? = null
    private var listener: ValueEventListener? = null
    private var isFirstValue = true
    private var previousSos = false

    fun start() {
        createNotificationChannel()

        sosRef = FirebaseDatabase.getInstance("https://healthmonitorapp-bb874-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Users/user_123/live_data/sos_alert")

        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val current = snapshot.getValue(Boolean::class.java) ?: false

                Log.d("SosMonitor", "sos_alert state change: current=$current, previous=$previousSos, isFirst=$isFirstValue")

                if (isFirstValue) {
                    isFirstValue = false
                    previousSos = current
                    return
                }

                // Chỉ hiện thông báo khi có sự chuyển đổi duy nhất từ false -> true
                if (!previousSos && current) {
                    showSosNotification()
                }

                previousSos = current
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SosMonitor", "Không đọc được SOS từ Firebase: ${error.message}")
            }
        }

        sosRef?.addValueEventListener(listener as ValueEventListener)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cảnh báo SOS Khẩn cấp",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Cảnh báo khi người dùng nhấn nút SOS"
                enableVibration(true)
                setShowBadge(true)
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            manager?.createNotificationChannel(channel)
        }
    }

    private fun showSosNotification() {
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            Log.w("SosMonitor", "Chưa được cấp quyền thông báo POST_NOTIFICATIONS")
            return
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle("🚨 CẢNH BÁO SOS KHẨN CẤP!")
            .setContentText("Người dùng cần trợ giúp ngay lập tức. Hãy kiểm tra ứng dụng!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0, 500, 250, 500))

        try {
            // Dùng ID cố định 1001 để đè thông báo cũ, không làm tràn thông báo
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, builder.build())
            Log.i("SosMonitor", "Đã phát thông báo SOS duy nhất thành công với ID $NOTIFICATION_ID")
        } catch (e: Exception) {
            Log.e("SosMonitor", "Lỗi khi hiển thị thông báo SOS", e)
        }
    }

    fun stop() {
        listener?.let { sosRef?.removeEventListener(it) }
        listener = null
        sosRef = null
        isFirstValue = true
        previousSos = false
    }
}
