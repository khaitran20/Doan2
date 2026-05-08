package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar; // 🔄 Thêm ProgressBar
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient; // 🛡️ Thêm OkHttpClient
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangSuaTT extends AppCompatActivity {
    EditText edt_hoten, edt_ngaysinh, edt_sdt, edt_diachi, edt_matkhau, edt_maukhau2;
    Button btn_huy, btn_xacnhan;
    ProgressBar progressBar; // 🔄 Khai báo biến
    String matkhau;
    String mkc, hoten, ngaysinh, sdt, diachi;
    int makhachhang, mataikhoan;
    ketnoi Ketnoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_sua_tt);

        // Ánh xạ các View
        anhXaView();

        // 🛡️ Cấu hình OkHttpClient để chống lỗi Connection Reset
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://baigiuxe.elementfx.com/")
                .client(okHttpClient) // Gắn client vào đây
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Ketnoi = retrofit.create(ketnoi.class);

        SharedPreferences sharedPreferences = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("taikhoan", "");

        // Tải thông tin ban đầu
        LayThongTinNguoiDung(taikhoan);

        btn_huy.setOnClickListener(v -> {
            Intent intent = new Intent(TrangSuaTT.this, TrangTaiKhoan.class);
            startActivity(intent);
            finish();
        });

        btn_xacnhan.setOnClickListener(v -> {
            xuLyCapNhat();
        });
    }

    private void anhXaView() {
        edt_hoten = findViewById(R.id.edt_hoten);
        edt_ngaysinh = findViewById(R.id.edt_ngaysinh);
        edt_sdt = findViewById(R.id.edt_sdt);
        edt_diachi = findViewById(R.id.edt_diachi);
        edt_matkhau = findViewById(R.id.edt_matkhau);
        edt_maukhau2 = findViewById(R.id.edt_maukhau2);
        btn_huy = findViewById(R.id.btn_huy);
        btn_xacnhan = findViewById(R.id.btn_xacnhan);
        progressBar = findViewById(R.id.progressBar); // 🔄 Nhớ thêm ID này vào XML
    }

    private void xuLyCapNhat() {
        String s_mk1 = edt_matkhau.getText().toString().trim();
        String s_mk2 = edt_maukhau2.getText().toString().trim();
        hoten = edt_hoten.getText().toString().trim();
        ngaysinh = edt_ngaysinh.getText().toString().trim();
        sdt = edt_sdt.getText().toString().trim();
        diachi = edt_diachi.getText().toString().trim();

        if (s_mk1.isEmpty() && s_mk2.isEmpty()) {
            mkc = matkhau;
        } else if (!s_mk1.equals(s_mk2)) {
            Toast.makeText(this, "Mật khẩu không trùng khớp !!!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mkc = s_mk1;
        }

        SuaTTNguoiDung(makhachhang, mataikhoan, hoten, sdt, ngaysinh, diachi, mkc);
    }

    public void LayThongTinNguoiDung(String tk) {
        progressBar.setVisibility(View.VISIBLE); // 🔄 Hiện khi bắt đầu tải
        Ketnoi.LayThongTinNguoiDung(tk).enqueue(new Callback<TTNguoiDung>() {
            @Override
            public void onResponse(Call<TTNguoiDung> call, Response<TTNguoiDung> response) {
                progressBar.setVisibility(View.GONE); // 🔄 Ẩn khi xong
                if (response.isSuccessful() && response.body() != null) {
                    TTNguoiDung ttNguoiDung = response.body();
                    makhachhang = ttNguoiDung.getMaKhachHang();
                    mataikhoan = ttNguoiDung.getMaTaiKhoan();
                    edt_hoten.setText(ttNguoiDung.getTenKhachHang());
                    edt_ngaysinh.setText(ttNguoiDung.getNgaySinh());
                    edt_sdt.setText(ttNguoiDung.getSoDienThoai());
                    edt_diachi.setText(ttNguoiDung.getDiaChi());
                    matkhau = ttNguoiDung.getMatKhau();
                }
            }

            @Override
            public void onFailure(Call<TTNguoiDung> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangSuaTT.this, "Lỗi tải thông tin!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void SuaTTNguoiDung(int makh, int matk, String hoten, String sdt, String ns, String dc, String mk) {
        progressBar.setVisibility(View.VISIBLE); // 🔄 Hiện khi bắt đầu lưu
        Ketnoi.SuaTTNguoiDung(makh, matk, hoten, sdt, ns, dc, mk).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE); // 🔄 Ẩn khi xong
                if (response.isSuccessful() && response.body() != null && response.body().equals("1")) {
                    Toast.makeText(TrangSuaTT.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TrangSuaTT.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangSuaTT.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}