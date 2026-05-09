<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php"; // File chứa thông số kết nối TiDB Cloud

$data = json_decode(file_get_contents('php://input'), true);
$user_input = trim($_POST['taikhoan'] ?? ($data['taikhoan'] ?? ''));
$pass_input = trim($_POST['matkhau'] ?? ($data['matkhau'] ?? ''));

$response = array();

if (empty($user_input) || empty($pass_input)) {
    $response['success'] = 0;
    $response['message'] = "Vui lòng nhập đủ thông tin";
    echo json_encode($response);
    exit();
}

$user_input = mysqli_real_escape_string($conn, $user_input);
$pass_input = mysqli_real_escape_string($conn, $pass_input);

$sql = "SELECT * FROM taikhoan WHERE TenDangNhap = '$user_input' AND MatKhau = '$pass_input'";
$result = mysqli_query($conn, $sql);

if ($result && mysqli_num_rows($result) > 0) {
    $response['success'] = 1;
    $response['message'] = "Đăng nhập thành công";
} else {
    $response['success'] = 0;
    $response['message'] = "Tài khoản hoặc mật khẩu sai";
}

echo json_encode($response);
?>