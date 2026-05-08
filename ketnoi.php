<?php
$host = 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com';
$port = '4000';
$user = '3teo7ak1vNGCXsh.root';
$pass = '4jF8w08ARR0uREsg';
$dbname = 'test';

// Khởi tạo đối tượng mysqli
$conn = mysqli_init();

// Cấu hình SSL (Bắt buộc đối với TiDB Cloud Serverless)
mysqli_ssl_set($conn, NULL, NULL, NULL, NULL, NULL);

// Thực hiện kết nối
if (!mysqli_real_connect($conn, $host, $user, $pass, $dbname, $port, NULL, MYSQLI_CLIENT_SSL)) {
    die("Kết nối thất bại: " . mysqli_connect_error());
}

// Thiết lập tiếng Việt
mysqli_set_charset($conn, 'utf8');

// Khi chạy thực tế trên App, bạn nên XÓA hoặc COMMENT dòng echo này lại
// echo "Kết nối CSDL thành công!"; 
?>