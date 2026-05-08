<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";

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