# Triển khai Ứng dụng Sức Khỏe & An Toàn (Jetpack Compose)

Xây dựng 4 giao diện hoàn chỉnh bằng Jetpack Compose dựa trên các hình ảnh thiết kế cung cấp:
1. **Màn hình Trang chủ (Home)**: Hiển thị thông tin người dùng, lưới các chỉ số sức khỏe (Nhịp tim, SpO2, Nhiệt độ cơ thể, Căng thẳng, Cảnh báo té ngã, Vị trí hiện tại), nút SOS khẩn cấp và thanh điều hướng dưới.
2. **Màn hình Chi tiết Nhịp tim & Nồng độ Oxy**: Biểu đồ, vòng tròn chỉ số, tab Hiện tại/Lịch sử và đánh giá trạng thái.
3. **Màn hình Chi tiết Phát hiện Căng thẳng**: Mức độ căng thẳng, thanh phân loại (Thấp, Trung bình, Cao), biểu đồ chỉ số và lời khuyên sức khỏe.
4. **Màn hình Chi tiết Cảnh báo Té ngã**: Trạng thái hệ thống, công tắc bật/tắt phát hiện té ngã tự động, lịch sử cảnh báo và nút micro cứu hộ.

## User Review Required

> [!NOTE]
> Các màn hình sẽ được tích hợp sẵn điều hướng (Navigation) bằng Jetpack Navigation hoặc State đơn giản để người dùng có thể bấm vào các thẻ trên Trang chủ để mở các màn hình chi tiết tương ứng và bấm nút quay lại (`<`) để về Trang chủ.

## Proposed Changes

### :app

#### [NEW] [Models & States](file:///D:/app%20suc%20khoe/app/src/main/java/com/example/appsuckhoe/ui/model/HealthData.kt)
- Định nghĩa các dữ liệu mẫu cho các chỉ số sức khỏe.

#### [NEW] [Home Screen](file:///D:/app%20suc%20khoe/app/src/main/java/com/example/appsuckhoe/ui/screens/HomeScreen.kt)
- Giao diện Trang chủ với thanh tiêu đề xanh đậm, thẻ thông tin cá nhân, lưới thẻ chỉ số (Nhịp tim, SpO2, Nhiệt độ, Căng thẳng, Té ngã, Vị trí), nút SOS và thanh điều hướng dưới.

#### [NEW] [Heart Rate & SpO2 Detail Screen](file:///D:/app%20suc%20khoe/app/src/main/java/com/example/appsuckhoe/ui/screens/HeartRateDetailScreen.kt)
- Giao diện chi tiết Nhịp tim và SpO2 với biểu đồ đường, vòng tròn chỉ số và tab chuyển đổi.

#### [NEW] [Stress Detail Screen](file:///D:/app%20suc%20khoe/app/src/main/java/com/example/appsuckhoe/ui/screens/StressDetailScreen.kt)
- Giao diện chi tiết mức độ căng thẳng với thanh phân loại màu và biểu đồ chỉ số.

#### [NEW] [Fall Detection Detail Screen](file:///D:/app%20suc%20khoe/app/src/main/java/com/example/appsuckhoe/ui/screens/FallDetectionDetailScreen.kt)
- Giao diện cảnh báo té ngã với trạng thái an toàn, công tắc tự động và lịch sử.

#### [MODIFY] [MainActivity.kt](file:///D:/app%20suc%20khoe/app/src/main/java/com/example/appsuckhoe/MainActivity.kt)
- Thiết lập NavHost để điều hướng giữa 4 màn hình và cấu hình Theme.

## Verification Plan

### Automated Tests
- Build project successfully using Gradle.

### Manual Verification
- Chạy ứng dụng trên thiết bị ảo / máy thật và kiểm tra chuyển đổi giữa màn hình chính và các màn hình chi tiết.
