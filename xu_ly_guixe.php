<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";

$conn->set_charset("utf8mb4");
$conn->query("SET time_zone = '+07:00'");

$userName = $_POST['maKhachHang'] ?? ''; 
$maXeSelected = $_POST['ma_xe'] ?? 0; 

// 1. Lấy thông tin khách hàng
$user_sql = "SELECT MaKhachHang, SoDu FROM khachhang WHERE MaTaiKhoan = 
             (SELECT MaTaiKhoan FROM taikhoan WHERE TenDangNhap = '$userName') LIMIT 1";
$resUser = $conn->query($user_sql);
$userData = $resUser->fetch_assoc();

if (!$userData) {
    echo json_encode(["status" => "ERROR", "detail" => "Không tìm thấy thông tin người dùng!"]);
    exit;
}

$maKH = $userData['MaKhachHang'];
$soDuHienTai = $userData['SoDu'];

// 2. Kiểm tra xe đang trong bãi hay chưa (Xử lý RA BÃI)
$sql_check = "SELECT l.*, k.Gia FROM luotguixe l
              JOIN vitridau v ON l.MaViTri = v.MaViTri
              JOIN khuvuc k ON v.MaKhuVuc = k.MaKhuVuc
              WHERE l.MaKhachHang = $maKH AND l.MaXe = $maXeSelected AND l.TrangThai = 'Đang gửi' LIMIT 1";
$result = $conn->query($sql_check);

if ($result && $result->num_rows > 0) {
    // --- KỊCH BẢN: RA BÃI ---
    $row = $result->fetch_assoc();
    $maLuot = $row['MaLuot'];
    $maViTriHienTai = $row['MaViTri'];

    // Tính toán thời gian và tiền bạc
    $sql_calc = "SELECT TIMESTAMPDIFF(SECOND, ThoiGianVao, NOW()) AS TongGiay FROM luotguixe WHERE MaLuot = $maLuot";
    $timeRes = $conn->query($sql_calc)->fetch_assoc();
    $tongGiay = (int)$timeRes['TongGiay'];
    $soNgay = ceil($tongGiay / 86400); 
    if ($soNgay <= 0) $soNgay = 1;
    $tongTien = $soNgay * (int)$row['Gia']; 

    if ($soDuHienTai < $tongTien) {
        echo json_encode(["status" => "FAILED_OUT", "detail" => "Số dư không đủ! Cần: " . number_format($tongTien) . "đ"]);
        exit;
    }

    $conn->begin_transaction();
    try {
        // 1. Trừ tiền
        $conn->query("UPDATE khachhang SET SoDu = SoDu - $tongTien WHERE MaKhachHang = $maKH");
        // 2. Cập nhật lượt gửi
        $conn->query("UPDATE luotguixe SET ThoiGianRa = NOW(), ThanhTien = $tongTien, TrangThai = 'Đã trả' WHERE MaLuot = $maLuot");
        // 3. Giải phóng vị trí (Cập nhật thành Trống)
        $conn->query("UPDATE vitridau SET TrangThai = 'Trống', MaKhachHang = 0, MaXe = 0 WHERE MaViTri = $maViTriHienTai");
        
        $conn->commit();
        echo json_encode([
            "status" => "PAYMENT_SUCCESS", 
            "detail" => "Thanh toán thành công cho $soNgay ngày. Mời xe ra bãi!",
            "tongTien" => number_format($tongTien)
        ]);
    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode(["status" => "ERROR", "detail" => "Lỗi xử lý thanh toán ra bãi!"]);
    }

} else {
    // --- KỊCH BẢN: VÀO BÃI ---
    
    // Bước A: Kiểm tra đặt chỗ trước
    $sql_booked = "SELECT p.MaPhieuDat, p.MaViTri, v.TenViTri, k.TenKhuVuc, k.Gia, k.BanDo 
                   FROM phieudatcho p 
                   JOIN vitridau v ON p.MaViTri = v.MaViTri
                   JOIN khuvuc k ON v.MaKhuVuc = k.MaKhuVuc
                   WHERE p.MaXe = $maXeSelected AND p.TrangThai = 'Chờ nhận chỗ' LIMIT 1";
    $resBooked = $conn->query($sql_booked);

    $isReserved = false;
    if ($resBooked && $resBooked->num_rows > 0) {
        $vtri = $resBooked->fetch_assoc();
        $maPhieuXoa = $vtri['MaPhieuDat'];
        $isReserved = true;
    } else {
        // Bước B: Nếu không đặt, tìm vị trí trống theo loại xe
        $sql_xe = "SELECT LoaiXe FROM xe WHERE MaXe = $maXeSelected LIMIT 1";
        $loaiXeRes = $conn->query($sql_xe)->fetch_assoc();
        $loaiXe = $loaiXeRes['LoaiXe'] ?? 'Xe máy';

        if (stripos($loaiXe, 'Ô tô') !== false) $targetKhu = 2;
        else if (stripos($loaiXe, 'Xe đạp') !== false) $targetKhu = 3;
        else $targetKhu = 1;

        $sql_empty = "SELECT v.*, k.TenKhuVuc, k.Gia, k.BanDo FROM vitridau v 
                      JOIN khuvuc k ON v.MaKhuVuc = k.MaKhuVuc
                      WHERE v.MaKhuVuc = $targetKhu AND v.TrangThai = 'Trống' 
                      ORDER BY v.MaViTri ASC LIMIT 1";
        $resEmpty = $conn->query($sql_empty);
        if ($resEmpty && $resEmpty->num_rows > 0) {
            $vtri = $resEmpty->fetch_assoc();
        } else {
            echo json_encode(["status" => "ERROR", "detail" => "Hết chỗ trống cho loại xe: " . $loaiXe]);
            exit;
        }
    }

    $conn->begin_transaction();
    try {
        $maVTriID = $vtri['MaViTri'];
        // 1. Ghi lượt gửi
        $conn->query("INSERT INTO luotguixe (MaKhachHang, MaViTri, MaXe, ThoiGianVao, TrangThai) VALUES ($maKH, $maVTriID, $maXeSelected, NOW(), 'Đang gửi')");
        // 2. Cập nhật vị trí thành Có xe
        $conn->query("UPDATE vitridau SET TrangThai = 'Có xe', MaKhachHang = $maKH, MaXe = $maXeSelected WHERE MaViTri = $maVTriID");
        
        $msgSuccess = "GỬI XE THÀNH CÔNG";
        if ($isReserved) {
            $conn->query("DELETE FROM phieudatcho WHERE MaPhieuDat = $maPhieuXoa");
            $msgSuccess .= " (Vị trí đặt trước)";
        }

        $conn->commit();
        echo json_encode([
            "status" => "IN_SUCCESS",
            "title" => $msgSuccess,
            "viTri" => $vtri['TenViTri'],
            "khuVuc" => $vtri['TenKhuVuc'],
            "gia" => number_format($vtri['Gia']) . "đ/Ngày",
            "huongDanKhu" => $vtri['BanDo'],
            "huongDanO" => $vtri['TenViTri']
        ]);
    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode(["status" => "ERROR", "detail" => "Lỗi ghi nhận vào bãi!"]);
    }
}
$conn->close();
?>