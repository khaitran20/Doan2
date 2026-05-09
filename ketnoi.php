<?php
$host = 'gateway01.ap-southeast-1.prod.aws.tidbcloud.com';
$port = '4000';
$user = '3teo7ak1vNGCXsh.root';
$pass = '4jF8w08ARR0uREsg';
$dbname = 'test';

$conn = mysqli_init();
mysqli_ssl_set($conn, NULL, NULL, NULL, NULL, NULL);

// Thực hiện kết nối
if (!mysqli_real_connect($conn, $host, $user, $pass, $dbname, $port, NULL, MYSQLI_CLIENT_SSL)) {
    // Nếu lỗi, trả về JSON để debug nhưng sau này nên tắt đi
    exit(); 
}
mysqli_set_charset($conn, 'utf8');
// KHÔNG echo gì ở đây
?>