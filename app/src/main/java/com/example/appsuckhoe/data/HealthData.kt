package com.example.appsuckhoe.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.PropertyName
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class HealthHistoryRecord(
    val recordId: String = UUID.randomUUID().toString(),
    val timestamp: Long = System.currentTimeMillis(),
    val userId: String = "user_123",
    val temperatureC: Double = 0.0,
    val heartRateBpm: Int = 0,
    val spo2Percent: Int = 0,
    val fallDetected: Boolean = false,
    val sos: Boolean = false,
    val stressLevel: String = "Không rõ",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val source: String = "ESP32-C3",
    val note: String = ""
)

data class HealthDataDto(
    @get:PropertyName("hr")
    @set:PropertyName("hr")
    var hr: Int = 72,

    @get:PropertyName("spo2")
    @set:PropertyName("spo2")
    var spo2: Int = 96,

    @get:PropertyName("temp")
    @set:PropertyName("temp")
    var temp: Double = 37.0,

    @get:PropertyName("fall_detected")
    @set:PropertyName("fall_detected")
    var fall_detected: Boolean = false,

    @get:PropertyName("sos_alert")
    @set:PropertyName("sos_alert")
    var sos_alert: Boolean = false,

    val stressLevel: String = "Thấp",
    val latitude: Double = 20.980272,
    val longitude: Double = 105.875923,
    val address: String = "Hoàng Mai, Hà Nội, Việt Nam",

    val hrHistory: List<Float> = emptyList(),
    val spO2History: List<Float> = emptyList(),
    val tempHistory: List<Float> = emptyList(),
    val stressHistory: List<Float> = emptyList()
) {
    val heartRate: Int get() = hr
    val sp02: Int get() = spo2
    val spO2: Int get() = spo2
    val bodyTemp: Double get() = temp
    val fallDetected: Boolean get() = fall_detected
}

class MedianSmoother(private val windowSize: Int = 5) {
    private val buffer = mutableListOf<Double>()

    fun add(value: Double?): Double? {
        if (value != null && value > 0) {
            buffer.add(value)
            if (buffer.size > windowSize) {
                buffer.removeAt(0)
            }
        }
        if (buffer.isEmpty()) return null
        val sorted = buffer.sorted()
        return sorted[sorted.size / 2]
    }
}

data class AppAlert(
    val type: String,
    val message: String,
    val isCritical: Boolean
)

class AlertEngine {
    private val counts = mutableMapOf<String, Int>()
    private val activeKeys = mutableSetOf<String>()
    private val requiredSustainedSamples = 3

    fun evaluate(data: HealthDataDto): List<AppAlert> {
        val alerts = mutableListOf<AppAlert>()

        fun evaluateSustained(key: String, condition: Boolean, message: String, critical: Boolean = false) {
            val currentCount = if (condition) (counts[key] ?: 0) + 1 else 0
            counts[key] = currentCount
            if (currentCount >= requiredSustainedSamples) {
                if (activeKeys.add(key)) {
                    alerts.add(AppAlert(key, message, critical))
                }
            } else {
                activeKeys.remove(key)
            }
        }

        fun evaluateInstant(key: String, condition: Boolean, message: String) {
            if (condition) {
                if (activeKeys.add(key)) {
                    alerts.add(AppAlert(key, message, true))
                }
            } else {
                activeKeys.remove(key)
            }
        }

        evaluateSustained("bpm_high", data.hr > 120, "Nhịp tim quá cao: ${data.hr} bpm", critical = true)
        evaluateSustained("bpm_low", data.hr in 1..49, "Nhịp tim quá thấp: ${data.hr} bpm", critical = true)
        evaluateSustained("spo2_low", data.spo2 in 1..91, "SpO₂ thấp: ${data.spo2}%", critical = true)
        evaluateSustained("temp_high", data.temp > 37.5, "Nhiệt độ cơ thể quá 37.5°C - Sốt: ${data.temp}°C", critical = true)
        evaluateInstant("fall", data.fall_detected, "Phát hiện sự cố té ngã!")
        evaluateInstant("sos", data.sos_alert, "Phát hiện tín hiệu khẩn cấp SOS!")

        return alerts
    }
}

object HealthFirebaseManager {
    private val database = FirebaseDatabase.getInstance("https://healthmonitorapp-bb874-default-rtdb.asia-southeast1.firebasedatabase.app")
    private val rootRef = database.reference

    private val _healthData = MutableStateFlow(HealthDataDto())
    val healthData: StateFlow<HealthDataDto> = _healthData.asStateFlow()

    private val _alerts = MutableStateFlow<List<AppAlert>>(emptyList())
    private val _historyRecords = MutableStateFlow<List<HealthHistoryRecord>>(emptyList())
    val historyRecords: StateFlow<List<HealthHistoryRecord>> = _historyRecords.asStateFlow()
    private val historyRef = database.getReference("Users/user_123/health_history")
    private var lastSavedSignature: String? = null
    val alerts: StateFlow<List<AppAlert>> = _alerts.asStateFlow()

    private val hrSmoother = MedianSmoother(5)
    private val spO2Smoother = MedianSmoother(5)
    private val tempSmoother = MedianSmoother(5)
    private val alertEngine = AlertEngine()

    private val mutableHrList = mutableListOf<Float>()
    private val mutableSpO2List = mutableListOf<Float>()
    private val mutableTempList = mutableListOf<Float>()
    private val mutableStressList = mutableListOf<Float>()

    init {
        Log.d("FirebaseDebug", "Initializing HealthFirebaseManager...")

        historyRef.orderByChild("timestamp").limitToLast(2000)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val records = snapshot.children.mapNotNull { child ->
                        child.getValue(HealthHistoryRecord::class.java)
                    }.sortedBy { it.timestamp }
                    _historyRecords.value = records
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("HealthHistory", "Không đọc được lịch sử: ${error.message}")
                }
            })

        rootRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val healthSnapshot = when {
                    snapshot.hasChild("Users/user_123/live_data") -> snapshot.child("Users/user_123/live_data")
                    snapshot.hasChild("live_data") -> snapshot.child("live_data")
                    snapshot.hasChild("SmartBand/User1") -> snapshot.child("SmartBand/User1")
                    else -> snapshot
                }

                try {
                    val rawHr = when (val v = healthSnapshot.child("hr").value ?: healthSnapshot.child("heartRate").value ?: healthSnapshot.child("BPM").value) {
                        is Long -> v.toDouble()
                        is Int -> v.toDouble()
                        is Double -> v
                        else -> 72.0
                    }

                    val rawSpO2 = when (val v = healthSnapshot.child("spo2").value ?: healthSnapshot.child("sp02").value ?: healthSnapshot.child("SpO2").value) {
                        is Long -> v.toDouble()
                        is Int -> v.toDouble()
                        is Double -> v
                        else -> 96.0
                    }

                    val rawTemp = when (val v = healthSnapshot.child("temp").value ?: healthSnapshot.child("bodyTemp").value ?: healthSnapshot.child("Temp").value) {
                        is Double -> v
                        is Float -> v.toDouble()
                        is Long -> v.toDouble()
                        is Int -> v.toDouble()
                        else -> 37.0
                    }

                    val hr = hrSmoother.add(rawHr)?.toInt() ?: rawHr.toInt()
                    val spo2 = spO2Smoother.add(rawSpO2)?.toInt() ?: rawSpO2.toInt()
                    val temp = tempSmoother.add(rawTemp) ?: rawTemp

                    val fall = healthSnapshot.child("fall_detected").getValue(Boolean::class.java) 
                        ?: healthSnapshot.child("fallDetected").getValue(Boolean::class.java)
                        ?: healthSnapshot.child("Fall").getValue(Boolean::class.java) ?: false

                    val sos = healthSnapshot.child("sos_alert").getValue(Boolean::class.java)
                        ?: healthSnapshot.child("SOS").getValue(Boolean::class.java)
                        ?: healthSnapshot.child("AppSOS").getValue(Boolean::class.java) ?: false

                    val stress = healthSnapshot.child("stressLevel").getValue(String::class.java) 
                        ?: when (val s = healthSnapshot.child("Stress").value) {
                            0L, 0 -> "Thấp"
                            1L, 1 -> "Trung bình"
                            2L, 2 -> "Cao"
                            else -> "Thấp"
                        }

                    val stressVal = when (stress) {
                        "Thấp" -> 30f
                        "Trung bình" -> 60f
                        "Cao" -> 85f
                        else -> 30f
                    }

                    val lat = when (val v = healthSnapshot.child("latitude").value ?: healthSnapshot.child("Lat").value ?: healthSnapshot.child("PhoneLat").value) {
                        is Double -> v
                        is Float -> v.toDouble()
                        is Long -> v.toDouble()
                        is Int -> v.toDouble()
                        else -> 20.980272
                    }

                    val lon = when (val v = healthSnapshot.child("longitude").value ?: healthSnapshot.child("Lng").value ?: healthSnapshot.child("PhoneLon").value) {
                        is Double -> v
                        is Float -> v.toDouble()
                        is Long -> v.toDouble()
                        is Int -> v.toDouble()
                        else -> 105.875923
                    }

                    val addr = healthSnapshot.child("address").getValue(String::class.java) ?: "Hoàng Mai, Hà Nội, Việt Nam"

                    if (mutableHrList.isEmpty() || mutableHrList.last() != hr.toFloat()) {
                        mutableHrList.add(hr.toFloat())
                        if (mutableHrList.size > 15) mutableHrList.removeAt(0)
                    }

                    if (mutableSpO2List.isEmpty() || mutableSpO2List.last() != spo2.toFloat()) {
                        mutableSpO2List.add(spo2.toFloat())
                        if (mutableSpO2List.size > 15) mutableSpO2List.removeAt(0)
                    }

                    if (mutableTempList.isEmpty() || mutableTempList.last() != temp.toFloat()) {
                        mutableTempList.add(temp.toFloat())
                        if (mutableTempList.size > 15) mutableTempList.removeAt(0)
                    }

                    if (mutableStressList.isEmpty() || mutableStressList.last() != stressVal) {
                        mutableStressList.add(stressVal)
                        if (mutableStressList.size > 15) mutableStressList.removeAt(0)
                    }

                    val signature = "$hr|$spo2|${"%.2f".format(java.util.Locale.US, temp)}|$fall|$sos|$stress|$lat|$lon"
                    if (signature != lastSavedSignature && (hr > 0 || spo2 > 0 || temp > 0.0)) {
                        lastSavedSignature = signature
                        val record = HealthHistoryRecord(
                            timestamp = System.currentTimeMillis(),
                            temperatureC = temp,
                            heartRateBpm = hr,
                            spo2Percent = spo2,
                            fallDetected = fall,
                            sos = sos,
                            stressLevel = stress,
                            latitude = lat,
                            longitude = lon,
                            note = if (fall || sos || hr > 100 || spo2 in 1..94 || temp > 37.5) "Có chỉ số cần chú ý" else ""
                        )
                        val recordRef = historyRef.push()
                        val savedRecord = record.copy(recordId = recordRef.key ?: record.recordId)
                        recordRef.setValue(savedRecord)
                            .addOnSuccessListener {
                                GoogleSheetsSync.sync(savedRecord)
                            }
                            .addOnFailureListener { e -> Log.e("HealthHistory", "Lưu lịch sử thất bại", e) }
                    }

                    val newData = HealthDataDto(
                        hr = hr,
                        spo2 = spo2,
                        temp = temp,
                        fall_detected = fall,
                        sos_alert = sos,
                        stressLevel = stress,
                        latitude = lat,
                        longitude = lon,
                        address = addr,
                        hrHistory = mutableHrList.toList(),
                        spO2History = mutableSpO2List.toList(),
                        tempHistory = mutableTempList.toList(),
                        stressHistory = mutableStressList.toList()
                    )

                    val evaluatedAlerts = alertEngine.evaluate(newData)
                    _alerts.value = evaluatedAlerts
                    _healthData.value = newData
                } catch (e: Exception) {
                    Log.e("FirebaseDebug", "Error parsing ESP32 snapshot", e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseDebug", "onCancelled: ${error.message}", error.toException())
            }
        })
    }

    fun updateData(newData: HealthDataDto) {
        try {
            database.getReference("Users/user_123/live_data").setValue(newData)
        } catch (e: Exception) {
            Log.e("FirebaseDebug", "Error updating data", e)
        }
    }

    fun updateMetric(key: String, value: Any) {
        try {
            database.getReference("Users/user_123/live_data").child(key).setValue(value)
            database.getReference("SmartBand/User1").child(key).setValue(value)
        } catch (e: Exception) {
            Log.e("FirebaseDebug", "Error updating metric $key", e)
        }
    }
}
