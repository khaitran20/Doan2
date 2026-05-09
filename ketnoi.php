<?php
$host = 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com';
$port = '4000';
$user = '3teo7ak1vNGCXsh.root';
$pass = 'IkP13ElD2JZhPeKY'; // Đã cập nhật theo hình ảnh bạn gửi
$dbname = 'test'; // Đảm bảo các bảng của bãi xe nằm trong db này

$conn = mysqli_init();

mysqli_ssl_set($conn, NULL, NULL, NULL, NULL, NULL);


error_reporting(0);

if (!mysqli_real_connect($conn, $host, $user, $pass, $dbname, $port, NULL, MYSQLI_CLIENT_SSL)) {

    exit(); 
}

mysqli_set_charset($conn, 'utf8');
echo "Kết nối TiDB thành công!";
?>