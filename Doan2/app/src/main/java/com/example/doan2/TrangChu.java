package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangChu extends AppCompatActivity {
    // Khai báo các View
    TextView txtTenNguoiDung, txtSoDu;
    ImageButton imgBtnXeDaGui, btn_taikhoan, img_btn_datcho, img_btn_phuongtien, img_btn_qr, img_btn_parking;
    AppCompatButton btnTextXeDaGui, btn_text_datcho, btn_text_phuongtien, btn_text_qr, btn_text_parking, btn_naptien;

    ProgressBar progressBar; // 🔄 Vòng xoay tải dữ liệu
    ketnoi Ketnoi;

    private boolean isNavigating = false; // 🔒 Biến cờ ngăn nhấn nút liên tục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_chu);

        // Xử lý Padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ View
        initViews();

        // 2. Cấu hình Retrofit với OkHttpClient để tăng độ ổn định
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://doan2-qhh0.onrender.com/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Ketnoi = retrofit.create(ketnoi.class);

        // 3. Lấy dữ liệu người dùng ban đầu
        SharedPreferences sharedPreferences = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("taikhoan", "");
        LayThongTinNguoiDung(taikhoan);

        // 4. Thiết lập sự kiện Click an toàn
        setupClickListeners();
    }

    private void initViews() {
        txtTenNguoiDung = findViewById(R.id.txtTenNguoiDung);
        txtSoDu = findViewById(R.id.txtSoDu);
        progressBar = findViewById(R.id.progressBar);

        btn_taikhoan = findViewById(R.id.btn_taikhoan);
        btn_naptien = findViewById(R.id.btn_naptien);

        img_btn_datcho = findViewById(R.id.img_btn_datcho);
        btn_text_datcho = findViewById(R.id.btn_text_datcho);

        img_btn_phuongtien = findViewById(R.id.img_btn_phuongtien);
        btn_text_phuongtien = findViewById(R.id.btn_text_phuongtien);

        img_btn_qr = findViewById(R.id.img_btn_qr);
        btn_text_qr = findViewById(R.id.btn_text_qr);

        img_btn_parking = findViewById(R.id.img_btn_parking);
        btn_text_parking = findViewById(R.id.btn_text_parking);

        imgBtnXeDaGui = findViewById(R.id.img_btn_xe_da_gui);
        btnTextXeDaGui = findViewById(R.id.btn_text_xe_da_gui);
    }

    private void setupClickListeners() {
        // Sử dụng hàm navigateTo để gom nhóm logic
        btn_taikhoan.setOnClickListener(v -> navigateTo(TrangTaiKhoan.class, true));
        btn_naptien.setOnClickListener(v -> navigateTo(TrangNapTien.class, true));

        img_btn_datcho.setOnClickListener(v -> navigateTo(TrangDatCho.class, true));
        btn_text_datcho.setOnClickListener(v -> navigateTo(TrangDatCho.class, true));

        img_btn_phuongtien.setOnClickListener(v -> navigateTo(TrangPhuongTien.class, true));
        btn_text_phuongtien.setOnClickListener(v -> navigateTo(TrangPhuongTien.class, true));

        img_btn_qr.setOnClickListener(v -> navigateTo(TrangGuiXe.class, true));
        btn_text_qr.setOnClickListener(v -> navigateTo(TrangGuiXe.class, true));

        img_btn_parking.setOnClickListener(v -> navigateTo(TrangBaiXe.class, true));
        btn_text_parking.setOnClickListener(v -> navigateTo(TrangBaiXe.class, true));

        imgBtnXeDaGui.setOnClickListener(v -> navigateTo(TrangXeDangGui.class, false));
        btnTextXeDaGui.setOnClickListener(v -> navigateTo(TrangXeDangGui.class, false));
    }

    // Hàm chuyển trang có kiểm soát
    private void navigateTo(Class<?> targetActivity, boolean shouldFinish) {
        if (isNavigating) return; // Nếu đang xử lý thì không cho nhấn tiếp

        isNavigating = true;
        progressBar.setVisibility(View.VISIBLE); // Hiện vòng xoay

        // Tạo một khoảng delay nhỏ (500ms) để UI mượt mà và tránh spam server
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(TrangChu.this, targetActivity);
            startActivity(intent);
            if (shouldFinish) finish();

            // Ẩn progress và mở khóa nút sau khi Intent đã khởi chạy
            progressBar.setVisibility(View.GONE);
            isNavigating = false;
        }, 500);
    }

    public void LayThongTinNguoiDung(String tk) {
        progressBar.setVisibility(View.VISIBLE);
        Ketnoi.LayThongTinNguoiDung(tk).enqueue(new Callback<TTNguoiDung>() {
            @Override
            public void onResponse(Call<TTNguoiDung> call, Response<TTNguoiDung> response) {
                progressBar.setVisibility(View.GONE);

                // Kiểm tra phản hồi có thành công và thân (body) có dữ liệu không
                if (response.isSuccessful() && response.body() != null) {
                    TTNguoiDung ttNguoiDung = response.body();

                    // Lớp bảo vệ 1: Kiểm tra Tên
                    if (ttNguoiDung.getTenKhachHang() != null) {
                        txtTenNguoiDung.setText(ttNguoiDung.getTenKhachHang());
                    } else {
                        txtTenNguoiDung.setText("Khách hàng mới");
                    }

                    // Lớp bảo vệ 2: Kiểm tra Số dư
                    if (ttNguoiDung.getSoDu() != null) {
                        txtSoDu.setText(String.format("%,.0f VNĐ", ttNguoiDung.getSoDu()));
                    } else {
                        txtSoDu.setText("0 VNĐ");
                    }
                } else {
                    Toast.makeText(TrangChu.this, "Server trả về dữ liệu trống", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TTNguoiDung> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(TrangChu.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }
}