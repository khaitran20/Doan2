<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";

// Đọc dữ liệu gửi lên (hỗ trợ cả JSON và Form Data)
$data = json_decode(file_get_contents('php://input'), true);
$user_input = trim($_POST['taikhoan'] ?? ($data['taikhoan'] ?? ''));
$pass_input = trim($_POST['matkhau'] ?? ($data['matkhau'] ?? ''));

if (empty($user_input) || empty($pass_input)) {
    echo "0"; // Trả về 0 nếu thiếu thông tin
    exit();
}

// Chống SQL Injection để bảo vệ bảng taikhoan
$user_input = mysqli_real_escape_string($conn, $user_input);
$pass_input = mysqli_real_escape_string($conn, $pass_input);

// Truy vấn kiểm tra từ bảng taikhoan trong database test
$sql = "SELECT * FROM taikhoan WHERE TenDangNhap = '$user_input' AND MatKhau = '$pass_input'";
$result = mysqli_query($conn, $sql);

if ($result && mysqli_num_rows($result) > 0) {
    // Chỉ trả về 1 khi khớp hoàn toàn thông tin
    echo "1"; 
} else {
    echo "0";
}
?>