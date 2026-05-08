<?php
header("Content-Type: application/json; charset=UTF-8");
// 1. Kết nối CSDL với thông tin từ x10Hosting
$host = "localhost";
$user = "lkzafhlb_qlbx"; 
$pass = "SYfdz2WKSgrCxSGd78L";
$db   = "lkzafhlb_qlbx";

$conn = mysqli_connect($host, $user, $pass, $db);
$conn->set_charset("utf8");

$maKhu = $_POST['MaKhuVuc'] ?? 1;

$sql = "SELECT * FROM khuvuc WHERE MaKhuVuc = '$maKhu'";
$res = $conn->query($sql);

if ($res && $res->num_rows > 0) {
    echo json_encode($res->fetch_assoc());
} else {
    echo json_encode(null);
}
$conn->close();
?>