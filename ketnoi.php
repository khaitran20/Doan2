<?php
$host = 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com';
$port = '4000';
$user = '3teo7ak1vNGCXsh.root';
$pass = '4jF8w08ARR0uREsg'; // Mật khẩu trong ảnh của bạn
$dbname = 'test'; // Mặc định TiDB có sẵn db 'test', bạn nên dùng db này cho nhanh

// Kết nối có thêm tham số Port
$conn = mysqli_connect($host, $user, $pass, $dbname, $port);

if (!$conn) {
    die("Kết nối thất bại: " . mysqli_connect_error());
}
echo "Kết nối CSDL thành công!";
?>