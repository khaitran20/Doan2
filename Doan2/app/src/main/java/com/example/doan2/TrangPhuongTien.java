package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar; // 🔄 Thêm ProgressBar
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient; // 🛡️ Thêm thư viện để cấu hình Timeout
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangPhuongTien extends AppCompatActivity {
    ImageButton btnBack;
    private RecyclerView recyclerViewPhuongTien;
    private View layoutTrong;
    private ProgressBar progressBar; // 🔄 Khai báo biến
    private ketnoi apiKetNoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_phuong_tien);

        anhXaView();
        khoiTaoRetrofit();
        taiDuLieuTuServer();

        btnBack = findViewById(R.id.button_quay_lai_phuong_tien);
        findViewById(R.id.button_them_phuong_tien).setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, TrangChu.class));
            finish();
        });
    }

    private void anhXaView() {
        recyclerViewPhuongTien = findViewById(R.id.recycler_view_phuong_tien);
        layoutTrong = findViewById(R.id.layout_danh_sach_trong);
        progressBar = findViewById(R.id.progressBar); // 🔄 Ánh xạ từ XML
        recyclerViewPhuongTien.setLayoutManager(new LinearLayoutManager(this));
    }

    private void khoiTaoRetrofit() {
        // 🛡️ Cấu hình OkHttpClient để tăng độ ổn định, tránh Connection Reset
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) // Tự động thử lại nếu đứt kết nối
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://baigiuxe.elementfx.com/")
                .client(client) // Gắn client vào Retrofit
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiKetNoi = retrofit.create(ketnoi.class);
    }

    private void taiDuLieuTuServer() {
        // 🔄 Hiện ProgressBar khi bắt đầu tải
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("taikhoan", "");

        if (!taikhoan.isEmpty()) {
            apiKetNoi.layDanhSachPhuongTien(taikhoan).enqueue(new Callback<List<Xe>>() {
                @Override
                public void onResponse(Call<List<Xe>> call, Response<List<Xe>> response) {
                    // 🔄 Ẩn ProgressBar khi có phản hồi
                    progressBar.setVisibility(View.GONE);

                    if (response.isSuccessful() && response.body() != null) {
                        capNhatHienThi(response.body());
                    }
                }

                @Override
                public void onFailure(Call<List<Xe>> call, Throwable t) {
                    // 🔄 Ẩn ProgressBar nếu lỗi xảy ra
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TrangPhuongTien.this, "Lỗi kết nối Server!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void capNhatHienThi(List<Xe> danhSach) {
        if (danhSach == null || danhSach.isEmpty()) {
            layoutTrong.setVisibility(View.VISIBLE);
            recyclerViewPhuongTien.setVisibility(View.GONE);
        } else {
            layoutTrong.setVisibility(View.GONE);
            recyclerViewPhuongTien.setVisibility(View.VISIBLE);

            recyclerViewPhuongTien.setAdapter(new XeAdapter(danhSach, this, new XeAdapter.OnXeClickListener() {
                @Override
                public void onItemSelected(Xe xe) {
                    SharedPreferences.Editor editor = getSharedPreferences("KHACH_HANG_DATA", MODE_PRIVATE).edit();
                    editor.putInt("ma_xe", xe.getMaXe());
                    editor.putString("ten_xe", xe.getTenXe());
                    editor.putString("bien_so", xe.getBienSo());
                    editor.putString("loai_xe", xe.getLoaiXe());
                    editor.putBoolean("da_chon_xe", true);
                    editor.apply();

                    Toast.makeText(TrangPhuongTien.this, "Đã chọn xe: " + xe.getTenXe(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemDelete(Xe xe) {
                    xoaPhuongTien(xe);
                }
            }));
        }
    }

    private void xoaPhuongTien(Xe xe) {
        // 🔄 Hiện ProgressBar khi đang thực hiện xóa
        progressBar.setVisibility(View.VISIBLE);

        apiKetNoi.xoaXe(xe.getMaXe()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Lưu ý: ProgressBar sẽ được hàm taiDuLieuTuServer quản lý khi tải lại danh sách
                Toast.makeText(TrangPhuongTien.this, "Đã xóa xe " + xe.getBienSo(), Toast.LENGTH_SHORT).show();
                taiDuLieuTuServer();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangPhuongTien.this, "Lỗi xóa xe!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}