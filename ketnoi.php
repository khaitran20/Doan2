<?php
$host = 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com';
$port = '4000';
$user = '3teo7ak1vNGCXsh.root';
$pass = 'IkP13ElD2JZhPeKY'; // Đã cập nhật theo hình ảnh bạn gửi
$dbname = 'test'; // Đảm bảo các bảng của bãi xe nằm trong db này

$conn = mysqli_init();

// Cấu hình SSL là bắt buộc đối với Public Endpoint của TiDB Cloud
mysqli_ssl_set($conn, NULL, NULL, NULL, NULL, NULL);

// Sử dụng error_reporting để ẩn các cảnh báo hệ thống có thể làm hỏng phản hồi 1/0
error_reporting(0);

if (!mysqli_real_connect($conn, $host, $user, $pass, $dbname, $port, NULL, MYSQLI_CLIENT_SSL)) {
    // Nếu kết nối thất bại, dừng lại ngay lập tức và không in lỗi Fatal
    exit(); 
}

mysqli_set_charset($conn, 'utf8');
// Tuyệt đối không echo bất kỳ dòng chữ nào tại đây
?>