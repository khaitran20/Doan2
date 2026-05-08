<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";



$conn->set_charset("utf8mb4");

$userName = $_POST['taikhoan'] ?? '';

// Truy vấn lấy danh sách xe đang gửi của khách hàng
$sql = "SELECT 
            v.TenViTri, 
            v.ViTri as ChiDan, 
            x.TenXe, 
            x.BienSo, 
            k.TenKhuVuc,
            k.Gia,
            l.ThoiGianVao,
            TIMESTAMPDIFF(SECOND, l.ThoiGianVao, NOW()) AS TongGiay
        FROM luotguixe l
        JOIN vitridau v ON l.MaViTri = v.MaViTri
        JOIN xe x ON l.MaXe = x.MaXe
        JOIN khuvuc k ON v.MaKhuVuc = k.MaKhuVuc
        JOIN khachhang kh ON l.MaKhachHang = kh.MaKhachHang
        JOIN taikhoan tk ON kh.MaTaiKhoan = tk.MaTaiKhoan
        WHERE tk.TenDangNhap = '$userName' AND l.TrangThai = 'Đang gửi'";

$result = $conn->query($sql);
$list = [];

while ($row = $result->fetch_assoc()) {
    // Tính toán tiền dựa trên số ngày (giống logic file quét mã)
    $soNgay = ceil($row['TongGiay'] / 86400);
    if ($soNgay <= 0) $soNgay = 1;
    $row['SoTienTamTinh'] = $soNgay * (int)$row['Gia'];
    $list[] = $row;
}

echo json_encode($list);
$conn->close();
?>