package com.example.doan2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangThemXe extends AppCompatActivity {
    Spinner spnLoaiXe;
    TextInputEditText edtBienSo, edtTenXe;
    TextInputLayout tilBienSo;
    ProgressBar progressBar;
    String[] options = {"Xe máy", "Xe đạp", "Xe ô tô"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_xe);

        initViews();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spnLoaiXe.setAdapter(adapter);

        // Logic tự động vô hiệu hóa biển số
        spnLoaiXe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {
                if (options[pos].equals("Xe đạp")) {
                    tilBienSo.setEnabled(false);
                    edtBienSo.setText(""); // Xóa nội dung nếu có
                    tilBienSo.setAlpha(0.5f);
                } else {
                    tilBienSo.setEnabled(true);
                    tilBienSo.setAlpha(1.0f);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> p) {}
        });

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_them_xe).setOnClickListener(v -> guiDuLieu());
    }

    private void initViews() {
        spnLoaiXe = findViewById(R.id.spn_loai_xe);
        edtBienSo = findViewById(R.id.edt_bien_so);
        edtTenXe = findViewById(R.id.edt_ten_xe);
        tilBienSo = findViewById(R.id.til_bien_so);
        progressBar = findViewById(R.id.progressBar);
    }

    private void guiDuLieu() {
        String loai = spnLoaiXe.getSelectedItem().toString();
        String bien = edtBienSo.getText().toString().trim();
        String ten = edtTenXe.getText().toString().trim();

        if (!loai.equals("Xe đạp") && bien.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập biển số!", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences pref = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String tk = pref.getString("taikhoan", "");

        progressBar.setVisibility(View.VISIBLE);
        // Gọi API themXe từ interface ketnoi
        RetrofitClient.getApi().themXe(bien, loai, ten, tk).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && "1".equals(response.body())) {
                    Toast.makeText(TrangThemXe.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(TrangThemXe.this, "Lỗi thêm xe!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangThemXe.this, "Lỗi kết nối!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}