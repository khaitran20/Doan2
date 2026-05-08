<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";

$user = $_POST['TenDangNhap'];
$pass = $_POST['MatKhau'];
$tenKH = $_POST['TenKhachHang'];
$sdt = $_POST['SoDienThoai'];
$ngaySinh = $_POST['NgaySinh'];
$diaChi = $_POST['DiaChi'];

// Mặc định cho Khách hàng mới
$loaiTK = "KHACHHANG";
$trangThai = "Hoạt động";
$soDuMacDinh = 0;

// Kiểm tra xem tên đăng nhập đã tồn tại chưa
$check = mysqli_query($conn, "SELECT * FROM taikhoan WHERE TenDangNhap = '$user'");
if(mysqli_num_rows($check) > 0) {
    echo "0"; // Tài khoản đã tồn tại
    exit();
}

// Bắt đầu Transaction
mysqli_begin_transaction($conn);

try {
    // 1. Thêm vào bảng taikhoan
    $sql1 = "INSERT INTO taikhoan (TenDangNhap, MatKhau, LoaiTaiKhoan, TrangThai) VALUES (?, ?, ?, ?)";
    $stmt1 = mysqli_prepare($conn, $sql1);
    mysqli_stmt_bind_param($stmt1, "ssss", $user, $pass, $loaiTK, $trangThai);
    mysqli_stmt_execute($stmt1);
    
    $maTaiKhoanMoi = mysqli_insert_id($conn);

    // 2. Thêm vào bảng khachhang
    $sql2 = "INSERT INTO khachhang (MaTaiKhoan, TenKhachHang, SoDienThoai, NgaySinh, DiaChi, SoDu) VALUES (?, ?, ?, ?, ?, ?)";
    $stmt2 = mysqli_prepare($conn, $sql2);
    mysqli_stmt_bind_param($stmt2, "issssd", $maTaiKhoanMoi, $tenKH, $sdt, $ngaySinh, $diaChi, $soDuMacDinh);
    mysqli_stmt_execute($stmt2);

    mysqli_commit($conn);
    echo "1"; // Thành công
} catch (Exception $e) {
    mysqli_rollback($conn);
    echo "0"; // Thất bại
}

mysqli_close($conn);
?>