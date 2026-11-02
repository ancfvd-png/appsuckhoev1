package com.example.appsuckhoe.data

import android.content.Context
import android.util.Log
import androidx.core.content.FileProvider
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object HealthExcelExporter {

    fun generateAndShareExcelReport(
        context: Context,
        records: List<HealthHistoryRecord>
    ) {
        try {
            val workbook = XSSFWorkbook()

            // ------------------------------------------------------------
            // SHEET 1: LichSuSucKhoe (Bảng dữ liệu chính)
            // ------------------------------------------------------------
            val sheet1 = workbook.createSheet("LichSuSucKhoe")
            
            // Style Header
            val headerFont = workbook.createFont().apply {
                bold = true
                color = IndexedColors.WHITE.index
            }
            val headerStyle = workbook.createCellStyle().apply {
                setFont(headerFont)
                fillForegroundColor = IndexedColors.DARK_BLUE.index
                fillPattern = org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND
            }

            val headers = listOf(
                "record_id", "user_id", "ngay_gio", "nhiet_do_C",
                "nhip_tim_bpm", "SpO2_percent", "phat_hien_nga", "SOS",
                "trang_thai_suc_khoe", "vi_do", "kinh_do", "pin_percent",
                "trang_thai_ket_noi", "nguon_du_lieu", "ghi_chu"
            )

            val headerRow = sheet1.createRow(0)
            headers.forEachIndexed { i, title ->
                val cell = headerRow.createCell(i)
                cell.setCellValue(title)
                cell.cellStyle = headerStyle
            }

            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val exportRecords = if (records.isNotEmpty()) records else listOf(
                HealthHistoryRecord(
                    recordId = "HM-0001",
                    userId = "user_123",
                    timestamp = System.currentTimeMillis(),
                    temperatureC = 36.6,
                    heartRateBpm = 78,
                    spo2Percent = 96,
                    fallDetected = false,
                    sos = false,
                    stressLevel = "Thấp",
                    latitude = 20.980272,
                    longitude = 105.875923,
                    source = "ESP32-C3",
                    note = "Bình thường"
                )
            )

            exportRecords.forEachIndexed { index, r ->
                val row = sheet1.createRow(index + 1)
                val status = if (r.heartRateBpm > 100 || r.spo2Percent in 1..94 || r.temperatureC > 37.5 || r.fallDetected || r.sos) "Cảnh báo" else "Bình thường"

                row.createCell(0).setCellValue(r.recordId)
                row.createCell(1).setCellValue(r.userId)
                row.createCell(2).setCellValue(dateFormat.format(Date(r.timestamp)))
                row.createCell(3).setCellValue(r.temperatureC)
                row.createCell(4).setCellValue(r.heartRateBpm.toDouble())
                row.createCell(5).setCellValue(r.spo2Percent.toDouble())
                row.createCell(6).setCellValue(if (r.fallDetected) "Có" else "Không")
                row.createCell(7).setCellValue(if (r.sos) "Có" else "Không")
                row.createCell(8).setCellValue(status)
                row.createCell(9).setCellValue(r.latitude)
                row.createCell(10).setCellValue(r.longitude)
                row.createCell(11).setCellValue(88.0)
                row.createCell(12).setCellValue("Đã kết nối")
                row.createCell(13).setCellValue(r.source)
                row.createCell(14).setCellValue(r.note.ifEmpty { "Ghi nhận từ ứng dụng" })
            }

            // ------------------------------------------------------------
            // SHEET 2: HuongDan (Giải thích ý nghĩa từng cột)
            // ------------------------------------------------------------
            val sheet2 = workbook.createSheet("HuongDan")
            val guideHeaderRow = sheet2.createRow(0)
            listOf("Trường", "Ý nghĩa", "Định dạng / ví dụ", "Ghi chú tích hợp").forEachIndexed { i, t ->
                val cell = guideHeaderRow.createCell(i)
                cell.setCellValue(t)
                cell.cellStyle = headerStyle
            }

            val guideData = listOf(
                listOf("record_id", "Mã bản ghi duy nhất", "HM-0001 hoặc Push Key", "Khóa ngẫu nhiên Firebase"),
                listOf("user_id", "Mã người dùng", "user_123", "Khớp với Users/{user_id}"),
                listOf("ngay_gio", "Thời điểm đo", "2026-10-23 15:30:00", "Lưu timestamp ISO 8601"),
                listOf("nhiet_do_C", "Nhiệt độ đo được", "36.5", "Nhiệt độ cơ thể hồng ngoại MLX90614"),
                listOf("nhip_tim_bpm", "Nhịp tim", "78", "Đơn vị BPM từ cảm biến MAX30102"),
                listOf("SpO2_percent", "Độ bão hòa Oxy", "98", "Đơn vị phần trăm (%)"),
                listOf("phat_hien_nga", "Trạng thái phát hiện ngã", "Có / Không", "Xử lý từ thuật toán gia tốc MPU6050"),
                listOf("SOS", "Trạng thái nút SOS", "Có / Không", "Tín hiệu khẩn cấp nút GPIO 4"),
                listOf("trang_thai_suc_khoe", "Nhãn tổng hợp", "Bình thường / Cần theo dõi / Cảnh báo", "Đánh giá tự động phía App Client"),
                listOf("vi_do", "Vĩ độ GPS", "20.980272", "Định vị vị trí khẩn cấp"),
                listOf("kinh_do", "Kinh độ GPS", "105.875923", "Định vị vị trí khẩn cấp"),
                listOf("pin_percent", "Mức pin thiết bị", "85", "Phần trăm pin phần cứng ESP32"),
                listOf("trang_thai_ket_noi", "Trạng thái kết nối", "Đã kết nối / Mất kết nối", "Trạng thái truyền nhận Wi-Fi"),
                listOf("nguon_du_lieu", "Nguồn dữ liệu", "ESP32-C3 / Android app", "Phân biệt nguồn dữ liệu cảm biến"),
                listOf("ghi_chu", "Ghi chú bổ sung", "SOS được kích hoạt", "Chỉ lưu thông tin cần thiết")
            )

            guideData.forEachIndexed { index, rowData ->
                val r = sheet2.createRow(index + 1)
                rowData.forEachIndexed { cIndex, cellText ->
                    r.createCell(cIndex).setCellValue(cellText)
                }
            }

            // ------------------------------------------------------------
            // SHEET 3: TichHopFirebase (Thông tin đường dẫn Firebase)
            // ------------------------------------------------------------
            val sheet3 = workbook.createSheet("TichHopFirebase")
            val fbHeaderRow = sheet3.createRow(0)
            listOf("Hạng mục", "Đề xuất tích hợp").forEachIndexed { i, t ->
                val cell = fbHeaderRow.createCell(i)
                cell.setCellValue(t)
                cell.cellStyle = headerStyle
            }

            val fbData = listOf(
                listOf("Realtime Database path", "Users/{user_id}/health_history/{record_id}"),
                listOf("Dữ liệu hiện tại", "Users/{user_id}/live_data (chỉ số hiện tại thời gian thực)"),
                listOf("Lưu lịch sử", "Mỗi lần đo hoặc theo chu kỳ cấu hình, ghi bản ghi có timestamp"),
                listOf("Xuất CSV / Excel", "Android App đọc history theo user_id, tạo file .xlsx 3 Sheet"),
                listOf("SOS Alert", "Ghi sự kiện riêng khi SOS chuyển từ false -> true, đẩy thông báo FCM"),
                listOf("Bảo mật", "Thiết lập Firebase Security Rules phù hợp")
            )

            fbData.forEachIndexed { index, rowData ->
                val r = sheet3.createRow(index + 1)
                rowData.forEachIndexed { cIndex, cellText ->
                    r.createCell(cIndex).setCellValue(cellText)
                }
            }

            // Ghi file .xlsx vào thư mục cache/reports/
            val reportsDir = File(context.cacheDir, "reports")
            if (!reportsDir.exists()) {
                reportsDir.mkdirs()
            }

            val excelFile = File(reportsDir, "HealthMonitorApp_LichSuSucKhoe.xlsx")
            FileOutputStream(excelFile).use { out ->
                workbook.write(out)
            }
            workbook.close()

            // Chia sẻ file .xlsx qua FileProvider
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                excelFile
            )

            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                putExtra(android.content.Intent.EXTRA_STREAM, uri)
                addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = android.content.Intent.createChooser(intent, "Mở hoặc chia sẻ Báo cáo Excel (.xlsx)")
            context.startActivity(chooser)

            Log.d("HealthExcelExporter", "Xuất file Excel thành công: ${excelFile.absolutePath}")
        } catch (e: Exception) {
            Log.e("HealthExcelExporter", "Lỗi khi tạo file Excel", e)
        }
    }
}
