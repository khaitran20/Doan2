package com.example.doan2;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangDangKy extends AppCompatActivity {
    TextInputEditText edtHoTen, edtSdt, edtNgaySinh, edtDiaChi, edtUser, edtPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_dang_ky);

        edtHoTen = findViewById(R.id.reg_hoten);
        edtSdt = findViewById(R.id.reg_sdt);
        edtNgaySinh = findViewById(R.id.reg_ngaysinh);
        edtDiaChi = findViewById(R.id.reg_diachi);
        edtUser = findViewById(R.id.reg_username);
        edtPass = findViewById(R.id.reg_password);
        ImageButton btnBack = findViewById(R.id.btn_back_dangky);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(TrangDangKy.this, MainActivity.class);
            finish();
        });

        // Hiển thị lịch khi chọn ngày sinh
        edtNgaySinh.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                edtNgaySinh.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });

        findViewById(R.id.btn_dangky).setOnClickListener(v -> xuLyDangKy());
    }

    private void xuLyDangKy() {
        String hoTen = edtHoTen.getText().toString().trim();
        String sdt = edtSdt.getText().toString().trim();
        String ngaySinh = edtNgaySinh.getText().toString().trim();
        String diaChi = edtDiaChi.getText().toString().trim();
        String user = edtUser.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();

        if(hoTen.isEmpty() || sdt.isEmpty() || user.isEmpty() || pass.isEmpty()){
            Toast.makeText(this, "Vui lòng nhập các thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API
        RetrofitClient.getApi().dangKy(user, pass, hoTen, sdt, ngaySinh, diaChi).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if ("1".equals(response.body())) {
                    Toast.makeText(TrangDangKy.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại trang đăng nhập
                } else {
                    Toast.makeText(TrangDangKy.this, "Tên đăng nhập đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<String> call, Throwable t) { }
        });
    }
}