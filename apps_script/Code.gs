/**
 * Google Apps Script tự động đồng bộ Firebase Realtime Database vào Google Trang tính (Google Sheets)
 * Dự án: HealthMonitorApp
 * Spreadsheet ID: 1yBGUXjuINTyEu1OS6VDbMmjiLsCJLtZWLPV3aRqNI2Q
 */

const SPREADSHEET_ID = "1yBGUXjuINTyEu1OS6VDbMmjiLsCJLtZWLPV3aRqNI2Q";
const FIREBASE_URL = "https://healthmonitorapp-bb874-default-rtdb.asia-southeast1.firebasedatabase.app";
const USER_ID = "user_123";

function getSpreadsheet() {
  if (SPREADSHEET_ID && SPREADSHEET_ID !== "") {
    return SpreadsheetApp.openById(SPREADSHEET_ID);
  }
  return SpreadsheetApp.getActiveSpreadsheet();
}

/**
 * Hàm khởi tạo tiêu đề 15 cột chuẩn theo template Excel cho Sheet LichSuSucKhoe
 */
function setupSheetsHeader() {
  const ss = getSpreadsheet();
  let sheet = ss.getSheetByName("LichSuSucKhoe");

  if (!sheet) {
    sheet = ss.insertSheet("LichSuSucKhoe");
  }

  const headers = [
    "record_id", "user_id", "ngay_gio", "nhiet_do_C",
    "nhip_tim_bpm", "SpO2_percent", "phat_hien_nga", "SOS",
    "trang_thai_suc_khoe", "vi_do", "kinh_do", "pin_percent",
    "trang_thai_ket_noi", "nguon_du_lieu", "ghi_chu"
  ];

  if (sheet.getLastRow() === 0) {
    sheet.appendRow(headers);
    sheet.getRange(1, 1, 1, headers.length).setFontWeight("bold").setBackground("#0D3B66").setFontColor("#FFFFFF");
  }
}

/**
 * Hàm tự động kéo dữ liệu từ Firebase Realtime Database và ghi vào Google Trang tính
 */
function syncFirebaseToSheet() {
  setupSheetsHeader();

  const url = `${FIREBASE_URL}/Users/${USER_ID}/live_data.json`;
  const response = UrlFetchApp.fetch(url);
  const data = JSON.parse(response.getContentText());

  if (!data) {
    Logger.log("Không tìm thấy dữ liệu trên Firebase!");
    return;
  }

  const ss = getSpreadsheet();
  const sheet = ss.getSheetByName("LichSuSucKhoe");

  const now = new Date();
  const timeFormatted = Utilities.formatDate(now, "Asia/Ho_Chi_Minh", "yyyy-MM-dd HH:mm:ss");

  const hr = data.hr || data.heartRate || data.BPM || 72;
  const spo2 = data.spo2 || data.sp02 || data.SpO2 || 96;
  const temp = data.temp || data.bodyTemp || data.Temp || 37.0;
  const fall = data.fall_detected || data.fallDetected || data.Fall || false;
  const sos = data.sos_alert || data.SOS || data.AppSOS || false;
  const lat = data.latitude || data.Lat || data.PhoneLat || 20.980272;
  const lon = data.longitude || data.Lng || data.PhoneLon || 105.875923;

  const status = (hr > 100 || spo2 < 95 || temp > 37.5 || fall || sos) ? "Cảnh báo" : "Bình thường";
  const recordId = "HM-" + now.getTime();

  const newRow = [
    recordId,
    USER_ID,
    timeFormatted,
    temp,
    hr,
    spo2,
    fall ? "Có" : "Không",
    sos ? "Có" : "Không",
    status,
    lat,
    lon,
    "88%",
    "Đã kết nối",
    "ESP32-C3",
    sos ? "BÁO ĐỘNG SOS" : (fall ? "SỰ CỐ TÉ NGÃ" : "Dữ liệu thời gian thực từ ESP32")
  ];

  sheet.appendRow(newRow);
  Logger.log("Đã đồng bộ bản ghi mới thành công vào Google Trang tính!");
}

/**
 * Web App Endpoint tiếp nhận yêu cầu POST/GET từ ứng dụng Android hoặc Webhook
 */
function doPost(e) {
  try {
    syncFirebaseToSheet();
    return ContentService.createTextOutput(JSON.stringify({ status: "success", message: "Ghi dữ liệu thành công!" }))
      .setMimeType(ContentService.MimeType.JSON);
  } catch (err) {
    return ContentService.createTextOutput(JSON.stringify({ status: "error", message: err.toString() }))
      .setMimeType(ContentService.MimeType.JSON);
  }
}

function doGet(e) {
  return doPost(e);
}
