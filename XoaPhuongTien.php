<?php
header("Content-Type: application/json; charset=UTF-8");
// 1. Kết nối CSDL với thông tin từ x10Hosting
$host = "localhost";
$user = "lkzafhlb_qlbx"; 
$pass = "SYfdz2WKSgrCxSGd78L";
$db   = "lkzafhlb_qlbx";

$conn = mysqli_connect($host, $user, $pass, $db);

$ma_xe = $_POST['MaXe'] ?? '';

if (!empty($ma_xe)) {
    // Thực hiện lệnh xóa dựa trên MaXe
    $sql = "DELETE FROM xe WHERE MaXe = '$ma_xe'";
    
    if ($conn->query($sql) === TRUE) {
        echo json_encode("Thành công");
    } else {
        echo json_encode("Lỗi: " . $conn->error);
    }
} else {
    echo json_encode("Thiếu mã xe");
}

$conn->close();
?>