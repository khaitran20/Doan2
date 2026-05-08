package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextInputEditText edt_taikhoan, edt_matkhau;
    Button btn_dangnhap;
    ProgressBar progressBar; // 🔄 Vòng xoay tải dữ liệu
    ketnoi ketnoiService;

    private boolean isLoggingIn = false; // 🔒 Cờ ngăn chặn nhấn nút liên tục

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Xử lý Padding cho giao diện EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ View
        edt_taikhoan = findViewById(R.id.edt_taikhoan);
        edt_matkhau = findViewById(R.id.edt_matkhau);
        btn_dangnhap = findViewById(R.id.btn_dangnhap);
        progressBar = findViewById(R.id.progressBar);

        // 1. Cấu hình OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        // 2. Cấu hình Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://doan2-qhh0.onrender.com/")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        ketnoiService = retrofit.create(ketnoi.class);

        btn_dangnhap.setOnClickListener(v -> {
            String taikhoan = edt_taikhoan.getText().toString().trim();
            String matkhau = edt_matkhau.getText().toString().trim();

            if (taikhoan.isEmpty() || matkhau.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                batDauDangNhap(taikhoan, matkhau);
            }
        });
    }

    private void batDauDangNhap(String tk, String mk) {
        if (isLoggingIn) return; // Nếu đang xử lý thì không làm gì cả

        isLoggingIn = true;
        progressBar.setVisibility(View.VISIBLE); // Hiện vòng xoay

        ketnoiService.kiemTraDangNhap(tk, mk).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // Tắt vòng xoay và mở khóa cờ khi có phản hồi
                progressBar.setVisibility(View.GONE);
                isLoggingIn = false;

                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().trim();

                    // So sánh chính xác chuỗi trả về có phải là "1" hay không
                    if (result.equals("1")) {
                        // Lưu tài khoản vào SharedPreferences để các trang sau dùng
                        SharedPreferences sharedPreferences = getSharedPreferences("nguoidung", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("taikhoan", tk);
                        editor.apply();

                        Toast.makeText(MainActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, TrangChu.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không chính xác", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Tắt vòng xoay và mở khóa cờ khi gặp lỗi kết nối
                progressBar.setVisibility(View.GONE);
                isLoggingIn = false;
                Toast.makeText(MainActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}