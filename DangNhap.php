<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";



mysqli_set_charset($conn, 'utf8');

if (!$conn) {
    echo json_encode(["status" => "error", "message" => "Không thể kết nối CSDL"]);
    exit();
}
// Thay cho các dòng lấy $_POST cũ
$data = json_decode(file_get_contents('php://input'), true);
$user_input = $_POST['taikhoan'] ?? ($data['taikhoan'] ?? '');
$pass_input = $_POST['matkhau'] ?? ($data['matkhau'] ?? '');
if (empty($user_input) || empty($pass_input)) {
    echo json_encode(["status" => "error", "message" => "Vui lòng nhập đủ thông tin"]);
    exit();
}

// 3. Truy vấn kiểm tra
$sql = "SELECT * FROM taikhoan WHERE TenDangNhap = '$user_input' AND MatKhau = '$pass_input'";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
    echo "1";
} else {
    echo "0";
}
?>