<?php
// Bật hiển thị mọi lỗi của PHP để kiểm tra
error_reporting(E_ALL);
ini_set('display_errors', 1);

include "ketnoi.php";

$user_input = $_POST['taikhoan'] ?? '';
$pass_input = $_POST['matkhau'] ?? '';

// Kiểm tra xem dữ liệu có lên đến PHP không
if (empty($user_input)) {
    die("Lỗi: PHP không nhận được biến 'taikhoan'. Hãy kiểm tra lại App.");
}

$sql = "SELECT * FROM taikhoan WHERE TenDangNhap = '$user_input' AND MatKhau = '$pass_input'";
$result = mysqli_query($conn, $sql);

if (!$result) {
    // Nếu câu lệnh SQL bị lỗi (sai tên bảng, sai cột...)
    die("Lỗi SQL: " . mysqli_error($conn));
}

if (mysqli_num_rows($result) > 0) {
    echo "1";
} else {
    echo "Lỗi: Sai tài khoản hoặc mật khẩu. Bạn vừa nhập: " . $user_input . " / " . $pass_input;
}
?>