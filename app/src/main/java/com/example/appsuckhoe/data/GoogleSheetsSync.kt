package com.example.appsuckhoe.data

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

/**
 * Sends each successfully saved Firebase history record to one fixed Google Spreadsheet.
 * Configure WEB_APP_URL and SHARED_SECRET after deploying the Apps Script.
 */
object GoogleSheetsSync {
    private const val TAG = "GoogleSheetsSync"

    // Replace with the deployed Apps Script Web App URL ending in /exec.
    private const val WEB_APP_URL = "https://script.google.com/macros/s/AKfycbx30TEeyG8_dzxHvoKMd8SLVyOi3YFVujp_qFoflSGpezrmjRnC8t_YJC4PSKc0aACUpQ/exec"
    // Must exactly match the SYNC_SECRET Script Property in Apps Script.
    private const val SHARED_SECRET = "123456789"

    private val executor = Executors.newSingleThreadExecutor()

    fun sync(record: HealthHistoryRecord) {
        if (WEB_APP_URL.startsWith("PASTE_") || SHARED_SECRET.startsWith("REPLACE_")) {
            Log.w(TAG, "Chưa cấu hình WEB_APP_URL/SHARED_SECRET; bỏ qua đồng bộ Sheets")
            return
        }

        executor.execute {
            var connection: HttpURLConnection? = null
            try {
                val payload = JSONObject().apply {
                    put("token", SHARED_SECRET)
                    put("record", JSONObject().apply {
                        put("record_id", record.recordId)
                        put("user_id", record.userId)
                        put("timestamp", record.timestamp)
                        put("nhiet_do_C", record.temperatureC)
                        put("nhip_tim_bpm", record.heartRateBpm)
                        put("SpO2_percent", record.spo2Percent)
                        put("phat_hien_nga", if (record.fallDetected) "Có" else "Không")
                        put("SOS", if (record.sos) "Có" else "Không")
                        put("trang_thai_suc_khoe", if (record.note.isNotBlank()) "Cần chú ý" else "Bình thường")
                        put("vi_do", record.latitude)
                        put("kinh_do", record.longitude)
                        put("pin_percent", "")
                        put("trang_thai_ket_noi", "Đã kết nối")
                        put("nguon_du_lieu", record.source)
                        put("ghi_chu", record.note)
                        put("stressLevel", record.stressLevel)
                    })
                }

                connection = (URL(WEB_APP_URL).openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    connectTimeout = 15000
                    readTimeout = 20000
                    doOutput = true
                    setRequestProperty("Content-Type", "application/json; charset=utf-8")
                }
                connection.outputStream.use { it.write(payload.toString().toByteArray(Charsets.UTF_8)) }
                val code = connection.responseCode
                val response = (if (code in 200..299) connection.inputStream else connection.errorStream)
                    ?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
                if (code in 200..299 && response.contains("\"ok\":true")) {
                    Log.i(TAG, "Đồng bộ record ${record.recordId} thành công")
                } else {
                    Log.e(TAG, "Đồng bộ Sheets thất bại HTTP $code: $response")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Lỗi gửi bản ghi lên Google Sheets", e)
            } finally {
                connection?.disconnect()
            }
        }
    }
}
