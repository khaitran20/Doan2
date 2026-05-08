<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Cổng Bãi Xe Thông Minh</title>
    <style>
        body {
            margin: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            background-color: #0f172a;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            color: white;
        }
        .container {
            text-align: center;
            background: #1e293b;
            padding: 50px;
            border-radius: 30px;
            box-shadow: 0 20px 50px rgba(0,0,0,0.5);
            border: 2px solid #334155;
        }
        .qr-wrapper {
            background: white;
            padding: 20px;
            border-radius: 20px;
            display: inline-block;
            margin-bottom: 20px;
        }
        h1 { margin: 10px 0; font-size: 2rem; color: #38bdf8; }
        p { color: #94a3b8; font-size: 1.1rem; }
        .footer {
            margin-top: 30px;
            display: flex;
            justify-content: space-around;
            font-weight: bold;
        }
        .tag {
            padding: 8px 15px;
            border-radius: 10px;
            font-size: 0.9rem;
        }
        .tag-a { background: #10b981; color: white; } /* Khu A */
        .tag-b { background: #f59e0b; color: white; } /* Khu B */
        .tag-c { background: #6366f1; color: white; } /* Khu C */
    </style>
</head>
<body>

    <div class="container">
        <div class="qr-wrapper">
            <!-- Sử dụng API để tạo mã QR với nội dung GATE_MAIN -->
            <img src="https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=GATE_MAIN" alt="Mã QR Cổng Chính">
        </div>
        
        <h1>QUÉT MÃ VÀO / RA</h1>
        <p>Hệ thống tự động xếp chỗ theo loại xe của bạn</p>

    </div>

</body>
</html>