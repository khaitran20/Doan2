<?php
ob_start(); // Thêm dòng này ở đầu file để tránh lỗi bộ đệm
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

// Lấy dữ liệu từ Android gửi lên
$user_input = $_POST['taikhoan'] ?? '';

if (empty($user_input)) {
    echo json_encode(["status" => "error", "message" => "Thiếu tham số taikhoan"]);
    exit();
}

// TRUY VẤN LẤY THÔNG TIN KHÁCH HÀNG QUA KHÓA NGOẠI
$sql = "SELECT k.* , t.*
        FROM taikhoan t 
        INNER JOIN khachhang k ON t.MaTaiKhoan = k.MaTaiKhoan 
        WHERE t.TenDangNhap = ?";

$stmt = mysqli_prepare($conn, $sql);

// LỖI 1: Bạn chỉ có 1 dấu '?' nhưng lại truyền 2 biến ($user_input, $pass_input)
// SỬA: Chỉ để lại "s" và $user_input
mysqli_stmt_bind_param($stmt, "s", $user_input);

mysqli_stmt_execute($stmt);
$result = mysqli_stmt_get_result($stmt);

if ($row = mysqli_fetch_assoc($result)) {
    ob_clean(); 
    // LỖI 2: Android của bạn đang đợi 1 Object TTNguoiDung trực tiếp, 
    // không phải là bọc trong ["data" => $row]
    // SỬA: Trả về thẳng $row
    echo json_encode($row);
} else {
    ob_clean();
    // Trả về một object rỗng hoặc lỗi tùy theo thiết kế của bạn
    echo json_encode(null);
}
mysqli_close($conn);
?>