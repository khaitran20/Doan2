<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";


$bienSo = $_POST['BienSo'] ?? '';
$loaiXe = $_POST['LoaiXe'] ?? '';
$tenXe  = $_POST['TenXe'] ?? '';
$tk     = $_POST['taikhoan'] ?? '';

// Lấy MaKhachHang dựa trên tài khoản
$sql_user = "SELECT MaKhachHang FROM khachhang k JOIN taikhoan t ON k.MaTaiKhoan = t.MaTaiKhoan WHERE t.TenDangNhap = ?";
$stmt_user = mysqli_prepare($conn, $sql_user);
mysqli_stmt_bind_param($stmt_user, "s", $tk);
mysqli_stmt_execute($stmt_user);
$res = mysqli_stmt_get_result($stmt_user);

if ($row = mysqli_fetch_assoc($res)) {
    $maKH = $row['MaKhachHang'];
    $sql_ins = "INSERT INTO xe (BienSo, LoaiXe, TenXe, MaKhachHang) VALUES (?, ?, ?, ?)";
    $stmt_ins = mysqli_prepare($conn, $sql_ins);
    mysqli_stmt_bind_param($stmt_ins, "sssi", $bienSo, $loaiXe, $tenXe, $maKH);
    
    if (mysqli_stmt_execute($stmt_ins)) {
        echo "1";
    } else {
        echo "0";
    }
} else {
    echo "0";
}
?>