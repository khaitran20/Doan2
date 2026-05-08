package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View; // 🔄 Cần thiết để điều khiển ẩn/hiện View
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar; // 🔄 Khai báo ProgressBar
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit; // 🛡️ Cấu hình thời gian chờ

import okhttp3.OkHttpClient; // 🛡️ Client kết nối mạng
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangTaiKhoan extends AppCompatActivity {

    TextView txt_ten, txt_ngaysinh, txt_sdt, txt_diachi;
    Button btn_thaydoitt, btn_lichsu;
    ImageButton btn_trangchu, btn_thongbao, btn_taikhoan, btn_dangxuat;
    ProgressBar progressBar; // 🔄 Biến điều khiển vòng xoay tải dữ liệu
    ketnoi Ketnoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_tai_khoan);

        // Thiết lập Padding cho System Bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXaView(); // Tách riêng phần ánh xạ để code gọn gàng hơn
        thietLapSuKien();

        // 🛡️ Bước 1: Cấu hình OkHttpClient để tăng độ ổn định kết nối
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) // Tự động thử lại nếu đứt kết nối
                .build();

        // Bước 2: Khởi tạo Retrofit với client đã cấu hình
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://baigiuxe.elementfx.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Ketnoi = retrofit.create(ketnoi.class);

        SharedPreferences sharedPreferences = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("taikhoan", "");

        LayThongTinNguoiDung(taikhoan);
    }

    private void anhXaView() {
        txt_ten = findViewById(R.id.txt_ten);
        txt_ngaysinh = findViewById(R.id.txt_ngaysinh);
        txt_sdt = findViewById(R.id.txt_sdt);
        txt_diachi = findViewById(R.id.txt_diachi);
        btn_thaydoitt = findViewById(R.id.btn_thaydoitt);
        btn_lichsu = findViewById(R.id.btn_lichsu);
        btn_trangchu = findViewById(R.id.btn_trangchu);
        btn_thongbao = findViewById(R.id.btn_thongbao);
        btn_taikhoan = findViewById(R.id.btn_taikhoan);
        btn_dangxuat = findViewById(R.id.btn_dangxuat);
        progressBar = findViewById(R.id.progressBar); // 🔄 Nhớ thêm ID này vào XML
    }

    private void thietLapSuKien() {
        btn_thaydoitt.setOnClickListener(v -> chuyenTrang(TrangSuaTT.class));
        btn_trangchu.setOnClickListener(v -> chuyenTrang(TrangChu.class));
        btn_lichsu.setOnClickListener(v -> chuyenTrang(TrangLichSuGuiXe.class));
        // Bạn có thể thêm sự kiện cho btn_dangxuat tại đây
    }

    private void chuyenTrang(Class<?> targetActivity) {
        Intent intent = new Intent(TrangTaiKhoan.this, targetActivity);
        startActivity(intent);
        finish();
    }

    public void LayThongTinNguoiDung(String tk) {
        // 🔄 Hiện ProgressBar khi bắt đầu gọi dữ liệu
        progressBar.setVisibility(View.VISIBLE);

        Ketnoi.LayThongTinNguoiDung(tk).enqueue(new Callback<TTNguoiDung>() {
            @Override
            public void onResponse(Call<TTNguoiDung> call, Response<TTNguoiDung> response) {
                // 🔄 Dù thành công hay lỗi HTTP, chúng ta cũng ẩn ProgressBar đi
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    TTNguoiDung ttNguoiDung = response.body();
                    txt_ten.setText(ttNguoiDung.getTenKhachHang());
                    txt_ngaysinh.setText(ttNguoiDung.getNgaySinh());
                    txt_sdt.setText(ttNguoiDung.getSoDienThoai());
                    txt_diachi.setText(ttNguoiDung.getDiaChi());
                } else {
                    Toast.makeText(TrangTaiKhoan.this, "Không lấy được thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TTNguoiDung> call, Throwable t) {
                // 🔄 Ẩn ProgressBar khi gặp lỗi kết nối
                progressBar.setVisibility(View.GONE);
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(TrangTaiKhoan.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}