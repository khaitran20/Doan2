<?php
$host = 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com';
$port = '4000';
$user = '3teo7ak1vNGCXsh.root';
$pass = '4jF8w08ARR0uREsg';
$dbname = 'test';

$conn = mysqli_init();

// Cấu hình SSL là bắt buộc cho TiDB Cloud Serverless
mysqli_ssl_set($conn, NULL, NULL, NULL, NULL, NULL);

// Thực hiện kết nối thực tế
if (!mysqli_real_connect($conn, $host, $user, $pass, $dbname, $port, NULL, MYSQLI_CLIENT_SSL)) {
    // Chỉ trả về lỗi dưới dạng JSON nếu không kết nối được
    header('Content-Type: application/json');
    die(json_encode(["status" => "error", "message" => "Database connection failed"]));
}

mysqli_set_charset($conn, 'utf8');
// Tuyệt đối không echo bất kỳ dòng chữ nào ở đây
?>