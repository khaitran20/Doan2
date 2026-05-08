package com.example.doan2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangBaiXe extends AppCompatActivity {
    TextView txtTenBai, txtGia, txtLoaiXe, txtSoLuong, txtMoTa, txtViTri;
    AppCompatButton btnKhuA, btnKhuB, btnKhuC;
    ImageButton btnBack;
    ProgressBar progressBar; // 🔄 Vòng xoay tải dữ liệu
    ketnoi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_bai_xe);

        anhXa();
        khoiTaoRetrofit();

        // 1. Mặc định vừa vào tải dữ liệu Khu A (ID=1)
        taiDuLieuKhuVuc(1);
        doiTrangThaiNut(btnKhuA);

        // 2. Sự kiện nhấn nút chọn khu vực
        btnKhuA.setOnClickListener(v -> { taiDuLieuKhuVuc(1); doiTrangThaiNut(btnKhuA); });
        btnKhuB.setOnClickListener(v -> { taiDuLieuKhuVuc(2); doiTrangThaiNut(btnKhuB); });
        btnKhuC.setOnClickListener(v -> { taiDuLieuKhuVuc(3); doiTrangThaiNut(btnKhuC); });

        // Nút quay lại
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, TrangChu.class));
            finish();
        });
    }

    private void taiDuLieuKhuVuc(int id) {
        // Hiện ProgressBar và khóa các nút để tránh người dùng nhấn liên tục 🛑
        progressBar.setVisibility(View.VISIBLE);
        setButtonsEnabled(false);

        api.getChiTietKhuVuc(id).enqueue(new Callback<KhuVuc>() {
            @Override
            public void onResponse(Call<KhuVuc> call, Response<KhuVuc> response) {
                // Ẩn ProgressBar và mở lại các nút khi có phản hồi từ server
                progressBar.setVisibility(View.GONE);
                setButtonsEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    KhuVuc kv = response.body();

                    // Đổ dữ liệu vào giao diện
                    txtTenBai.setText("Bãi xe " + kv.getTenKhuVuc());
                    txtGia.setText(String.format("%,.0fđ / ngày", kv.getGia()));
                    txtSoLuong.setText(kv.getSoLuongCho() + " chỗ");
                    txtMoTa.setText(kv.getMoTa());
                    txtViTri.setText("Chỉ dẫn: " + kv.getBanDo());

                    // Logic hiển thị loại xe dựa trên khu vực
                    if (id == 1) txtLoaiXe.setText("Xe máy");
                    else if (id == 2) txtLoaiXe.setText("Ô tô");
                    else txtLoaiXe.setText("Xe đạp");
                } else {
                    Toast.makeText(TrangBaiXe.this, "Không tìm thấy dữ liệu khu vực!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KhuVuc> call, Throwable t) {
                // Đảm bảo ẩn ProgressBar ngay cả khi gặp lỗi kết nối 🔌
                progressBar.setVisibility(View.GONE);
                setButtonsEnabled(true);
                Toast.makeText(TrangBaiXe.this, "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Hàm bật/tắt các nút chọn khu vực
    private void setButtonsEnabled(boolean status) {
        btnKhuA.setEnabled(status);
        btnKhuB.setEnabled(status);
        btnKhuC.setEnabled(status);
    }

    private void doiTrangThaiNut(AppCompatButton selected) {
        AppCompatButton[] list = {btnKhuA, btnKhuB, btnKhuC};
        for (AppCompatButton b : list) {
            b.setBackgroundResource(R.drawable.custom_button_unselected);
            b.setTextColor(Color.parseColor("#004AAD"));
        }
        selected.setBackgroundResource(R.drawable.custom_button_selected);
        selected.setTextColor(Color.WHITE);
    }

    private void anhXa() {
        txtTenBai = findViewById(R.id.txt_ten_bai_xe);
        txtGia = findViewById(R.id.txt_gia_gio);
        txtLoaiXe = findViewById(R.id.txt_loai_xe_phu);
        txtSoLuong = findViewById(R.id.txt_Soluong);
        txtMoTa = findViewById(R.id.txt_con_trong);
        txtViTri = findViewById(R.id.txt_mo_ta_vtri);
        btnKhuA = findViewById(R.id.btn_khuA);
        btnKhuB = findViewById(R.id.btn_khuB);
        btnKhuC = findViewById(R.id.btn_khuC);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar); // Đảm bảo ID này khớp với file XML
    }

    private void khoiTaoRetrofit() {
        // Sử dụng OkHttpClient để tăng độ ổn định cho kết nối 🛡️
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        api = new Retrofit.Builder()
                .baseUrl("https://doan2-qhh0.onrender.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ketnoi.class);
    }
}