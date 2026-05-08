<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";

$conn = mysqli_connect($host, $user, $pass, $db);
mysqli_set_charset($conn, 'utf8');

// Nhận dữ liệu
$makh = $_POST['MaKhachHang'] ?? '';
$matk = $_POST['MaTaiKhoan'] ?? '';
$hoten = $_POST['TenKhachHang'] ?? '';
$sdt = $_POST['SoDienThoai'] ?? '';
$diachi = $_POST['DiaChi'] ?? '';
$ngaysinh = $_POST['NgaySinh'] ?? '';
$matkhau = $_POST['MatKhau'] ?? '';

if (empty($makh)) {
    echo json_encode("0"); // Thất bại
    exit();
}

// 1. Cập nhật bảng khachhang
$sql1 = "UPDATE khachhang SET TenKhachHang=?, SoDienThoai=?, NgaySinh=?, DiaChi=? WHERE MaKhachHang=?";
$stmt1 = mysqli_prepare($conn, $sql1);
mysqli_stmt_bind_param($stmt1, "ssssi", $hoten, $sdt, $ngaysinh, $diachi, $makh);
$res1 = mysqli_stmt_execute($stmt1);

// 2. Cập nhật bảng taikhoan (nếu có đổi mật khẩu)
$sql2 = "UPDATE taikhoan SET MatKhau=? WHERE MaTaiKhoan=?";
$stmt2 = mysqli_prepare($conn, $sql2);
mysqli_stmt_bind_param($stmt2, "si", $matkhau, $matk);
$res2 = mysqli_stmt_execute($stmt2);

if ($res1 || $res2) {
    echo json_encode("1"); // Thành công
} else {
    echo json_encode("0");
}
mysqli_close($conn);
?>