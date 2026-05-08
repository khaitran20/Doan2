<?php
// Ngăn các cảnh báo nhỏ làm hỏng định dạng JSON trả về
error_reporting(0);
header("Content-Type: application/json; charset=UTF-8");
// 1. Kết nối CSDL với thông tin từ x10Hosting
$host = "localhost";
$user = "lkzafhlb_qlbx"; 
$pass = "SYfdz2WKSgrCxSGd78L";
$db   = "lkzafhlb_qlbx";

$conn = mysqli_connect($host, $user, $pass, $db);

// Kiểm tra kết nối
if ($conn->connect_error) {
    echo json_encode(["status" => "error", "message" => "Connect failed"]);
    exit();
}

$conn->set_charset("utf8");

// 2. Lấy dữ liệu từ POST (Retrofit gửi sang)
$user_input = $_POST['taikhoan'] ?? '';

if (!empty($user_input)) {
    // Câu lệnh SQL JOIN để khớp TenDangNhap từ bảng taikhoan với MaKhachHang trong bảng xe
    $sql = "SELECT xe.* FROM xe 
            INNER JOIN taikhoan ON xe.MaKhachHang = taikhoan.MaTaiKhoan 
            WHERE taikhoan.TenDangNhap = '$user_input'";
    $result = $conn->query($sql);
    $data = [];

    if ($result) {
        while($row = $result->fetch_assoc()) {
            $data[] = $row;
        }
        // Trả về danh sách xe (có thể là mảng rỗng [] nếu không có xe)
        echo json_encode($data);
    } else {
        echo json_encode(["status" => "error", "message" => "Query error"]);
    }
} else {
    // Trả về mảng rỗng nếu không nhận được tham số taikhoan
    echo json_encode([]);
}

$conn->close();
?>