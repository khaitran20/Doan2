<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// 1. Khai báo thông tin kết nối từ image_847a0b.png 📋
$host = "localhost";
$user = "lkzafhlb_baixe"; 
$pass = "QWTRxA5XNtCHLmcS95Cp"; // Mật khẩu chính xác từ ảnh 🔑
$db   = "lkzafhlb_baixe"; 

// 2. Thiết lập kết nối 🔌
$conn = mysqli_connect($host, $user, $pass, $db);

// 3. Kiểm tra kết nối 🛠️
if (!$conn) {
    die("Kết nối thất bại: " . mysqli_connect_error());
}

// 4. Cấu hình font chữ hiển thị tiếng Việt 🇻🇳
mysqli_set_charset($conn, 'utf8');


?>