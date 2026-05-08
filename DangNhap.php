<?php
header('Content-Type: application/json');

// 1. Kết nối CSDL với thông tin từ x10Hosting
$host = "localhost";
$user = "lkzafhlb_qlbx"; 
$pass = "SYfdz2WKSgrCxSGd78L";
$db   = "lkzafhlb_qlbx";

$conn = mysqli_connect($host, $user, $pass, $db);
mysqli_set_charset($conn, 'utf8');

if (!$conn) {
    echo json_encode(["status" => "error", "message" => "Không thể kết nối CSDL"]);
    exit();
}

// 2. Lấy dữ liệu từ App gửi lên
$user_input = $_POST['taikhoan'] ?? '';
$pass_input = $_POST['matkhau'] ?? '';

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