<?php
ob_start(); 
header('Content-Type: application/json; charset=utf-8');

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
$user_input = $_POST['taikhoan'] ?? ''; 

if (empty($user_input)) {

    echo json_encode([]); 
    exit();
}

$sql = "SELECT k.*, x.* FROM taikhoan t 
        INNER JOIN khachhang kh ON t.MaTaiKhoan = kh.MaTaiKhoan 
        INNER JOIN luotguixe k ON kh.MaKhachHang = k.MaKhachHang 
        INNER JOIN xe x ON k.MaXe = x.MaXe 
        WHERE t.TenDangNhap = ? 
        ORDER BY k.ThoiGianVao DESC";
$stmt = mysqli_prepare($conn, $sql);
mysqli_stmt_bind_param($stmt, "s", $user_input);
mysqli_stmt_execute($stmt);

$result_data = mysqli_stmt_get_result($stmt);

$result = array();
while ($row = mysqli_fetch_assoc($result_data)) {
    $result[] = $row;
}

echo json_encode($result);

mysqli_stmt_close($stmt);
mysqli_close($conn);
?>