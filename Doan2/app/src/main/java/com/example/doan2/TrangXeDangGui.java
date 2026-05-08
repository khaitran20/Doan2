package com.example.doan2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View; // 🔄 Cần để điều khiển ẩn/hiện ProgressBar
import android.widget.ImageButton;
import android.widget.ProgressBar; // 🔄 Khai báo ProgressBar
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangXeDangGui extends AppCompatActivity {
    RecyclerView rvXeDangGui;
    ImageButton btnBack;
    ProgressBar progressBar; // 🔄 Khai báo biến
    List<XeDangGui> dsXe = new ArrayList<>();
    XeDangGuiAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_xe_dang_gui);

        // Ánh xạ View
        rvXeDangGui = findViewById(R.id.rvXeDangGui);
        btnBack = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar); // 🔄 Nhớ thêm ID này vào XML

        rvXeDangGui.setLayoutManager(new LinearLayoutManager(this));

        btnBack.setOnClickListener(v -> finish()); // Đóng trang khi bấm quay lại

        loadData();
    }

    private void loadData() {
        // 🔄 Hiện ProgressBar khi bắt đầu tải dữ liệu
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences pref = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String user = pref.getString("taikhoan", "");

        if (user.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            return;
        }

        RetrofitClient.getApi().layXeDangGui(user).enqueue(new Callback<List<XeDangGui>>() {
            @Override
            public void onResponse(Call<List<XeDangGui>> call, Response<List<XeDangGui>> response) {
                // 🔄 Ẩn ProgressBar khi nhận được phản hồi
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    dsXe = response.body();
                    if (dsXe.isEmpty()) {
                        Toast.makeText(TrangXeDangGui.this, "Bạn hiện không có xe nào đang gửi", Toast.LENGTH_SHORT).show();
                    }
                    adapter = new XeDangGuiAdapter(dsXe);
                    rvXeDangGui.setAdapter(adapter);
                } else {
                    Toast.makeText(TrangXeDangGui.this, "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<XeDangGui>> call, Throwable t) {
                // 🔄 Ẩn ProgressBar nếu xảy ra lỗi kết nối
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangXeDangGui.this, "Lỗi kết nối máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}