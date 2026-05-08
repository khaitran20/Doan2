<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";



if ($conn->connect_error) {
    echo json_encode(["error" => "Connection failed"]);
    exit();
}
$conn->set_charset("utf8");

$action = $_POST['action'] ?? '';

if ($action == "get_spots") {
    $maKhu = (int)($_POST['MaKhuVuc'] ?? 0);
    // Xóa cột 'Gia' vì trong hình phpMyAdmin không có cột này
    $sql = "SELECT MaViTri, TenViTri, MaKhuVuc, TrangThai, MaKhachHang, MaXe, ViTri FROM vitridau WHERE MaKhuVuc = '$maKhu' AND TrangThai = 'Trống'";
    $res = $conn->query($sql);
    
    $data = [];
    if ($res) {
        while($row = $res->fetch_assoc()) { 
            // Ép kiểu các giá trị số về đúng kiểu int để GSON không bị lỗi
            $row['MaViTri'] = (int)$row['MaViTri'];
            $row['MaKhuVuc'] = (int)$row['MaKhuVuc'];
            $row['MaKhachHang'] = (int)$row['MaKhachHang'];
            $row['MaXe'] = (int)$row['MaXe'];
            $data[] = $row; 
        }
    }
    echo json_encode($data);
    exit();
}
if ($action == "book_spot") {
    $idViTri = (int)($_POST['MaViTri'] ?? 0);
    $maKH = (int)($_POST['MaKhachHang'] ?? 0);
    $maXe = (int)($_POST['MaXe'] ?? 0); 

    // KIỂM TRA 1: Xe này có đang chiếm chỗ nào trong bãi không?
    // Dựa vào hình của bạn, trạng thái khi có xe là "Có xe", khi đặt là "Đã đặt"
    $sql_check_vitri = "SELECT TenViTri FROM vitridau WHERE MaXe = '$maXe' AND TrangThai IN ('Đã đặt', 'Có xe') LIMIT 1";
    $res_check_vitri = $conn->query($sql_check_vitri);

    // KIỂM TRA 2: Xe này có phiếu đặt nào đang chờ (chưa quẹt thẻ) không?
    $sql_check_phieu = "SELECT MaPhieuDat FROM phieudatcho WHERE MaXe = '$maXe' AND TrangThai = 'Chờ nhận chỗ' LIMIT 1";
    $res_check_phieu = $conn->query($sql_check_phieu);

    if ($res_check_vitri->num_rows > 0 || $res_check_phieu->num_rows > 0) {
        // Nếu vi phạm 1 trong 2 điều kiện trên, trả về lỗi ngay lập tức
        echo json_encode("car_already_in_parking"); 
        exit();
    }

    // KIỂM TRA 3: Vị trí định đặt còn "Trống" không? (Phòng trường hợp người khác vừa đặt nhanh hơn)
    $sql_check_trong = "SELECT MaViTri FROM vitridau WHERE MaViTri = '$idViTri' AND TrangThai = 'Trống'";
    $res_trong = $conn->query($sql_check_trong);
    
    if ($res_trong->num_rows == 0) {
        echo json_encode("spot_taken"); 
        exit();
    }

    // Nếu vượt qua tất cả các lớp chặn trên thì mới tiến hành Transaction
    $conn->begin_transaction();
    try {
        // Cập nhật trạng thái vị trí đỗ
        $conn->query("UPDATE vitridau SET TrangThai = 'Đã đặt', MaKhachHang = '$maKH', MaXe = '$maXe' WHERE MaViTri = '$idViTri'");
        
        // Thêm phiếu đặt chỗ mới
        $conn->query("INSERT INTO phieudatcho (MaKhachHang, MaViTri, MaXe, ThoiGianDat, HanChotGiuCho, TrangThai) 
                       VALUES ('$maKH', '$idViTri', '$maXe', NOW(), DATE_ADD(NOW(), INTERVAL 30 MINUTE), 'Chờ nhận chỗ')");

        $conn->commit();
        echo json_encode("success"); 
    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode("error");
    }
    exit();
}
if ($action == "check_car_booked") {
    $maXe = (int)$_POST['MaXe'];
    // Quan trọng: Phải kiểm tra đúng trạng thái mà bạn lưu khi đặt thành công
    $sql = "SELECT p.MaPhieuDat, v.TenViTri 
            FROM phieudatcho p 
            JOIN vitridau v ON p.MaViTri = v.MaViTri 
            WHERE p.MaXe = '$maXe' AND p.TrangThai = 'Chờ nhận chỗ' 
            LIMIT 1";
    
    $result = $conn->query($sql);
    if ($result && $result->num_rows > 0) {
        echo json_encode($result->fetch_assoc());
    } else {
        // Trả về MaPhieuDat = 0 để Java biết là xe này đang rảnh
        echo json_encode(["MaPhieuDat" => 0]); 
    }
    exit();
}

// 4. Lấy danh sách phiếu đặt của khách hàng
if ($action == "get_list_phieu") {
    $maKH = (int)($_POST['MaKhachHang'] ?? 0);
    $sql = "SELECT p.*, v.TenViTri, v.ViTri as BanDo, x.TenXe, x.LoaiXe 
            FROM phieudatcho p
            JOIN vitridau v ON p.MaViTri = v.MaViTri
            JOIN xe x ON p.MaXe = x.MaXe
            WHERE p.MaKhachHang = '$maKH' AND p.TrangThai = 'Chờ nhận chỗ'
            ORDER BY p.ThoiGianDat DESC";
            
    $res = $conn->query($sql);
    $data = [];
    if ($res) {
        while($row = $res->fetch_assoc()) { $data[] = $row; }
    }
    echo json_encode($data);
    exit();
}

// 5. Xóa phiếu đặt chỗ (Hủy đặt)
if ($action == "delete_phieu") {
    $maPhieu = (int)($_POST['MaPhieu'] ?? 0);
    $maViTri = (int)($_POST['MaViTri'] ?? 0);

    $conn->begin_transaction();
    try {
        $conn->query("UPDATE phieudatcho SET TrangThai = 'Đã hủy' WHERE MaPhieuDat = '$maPhieu'");
        $conn->query("UPDATE vitridau SET TrangThai = 'Trống', MaKhachHang = 0, MaXe = 0 WHERE MaViTri = '$maViTri'");
        $conn->commit();
        echo json_encode("success");
    } catch (Exception $e) {
        $conn->rollback();
        echo json_encode("error");
    }
    exit();
}

$conn->close();
?>