<?php
header('Content-Type: application/json; charset=utf-8');
include "ketnoi.php";

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