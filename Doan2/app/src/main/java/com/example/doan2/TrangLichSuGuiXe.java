package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View; // Thêm View để điều khiển ẩn/hiện
import android.widget.ImageButton;
import android.widget.ProgressBar; // 🔄 Thêm ProgressBar
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit; // Thêm để cấu hình thời gian

import okhttp3.OkHttpClient; // 🛡️ Thêm OkHttpClient
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangLichSuGuiXe extends AppCompatActivity {
    RecyclerView rv_lichsu;
    LichSuAdapter adapter;
    List<LichSuGuiXe> lichSuList;
    ImageButton btn_back;
    ProgressBar progressBar; // 🔄 Khai báo ProgressBar
    ketnoi Ketnoi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trang_lich_su_gui_xe);

        // Ánh xạ View
        rv_lichsu = findViewById(R.id.rv_lichsu);
        btn_back = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar); // 🔄 Nhớ thêm ID này vào XML nhé

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(TrangLichSuGuiXe.this, TrangTaiKhoan.class);
            startActivity(intent);
            finish();
        });

        lichSuList = new ArrayList<>();
        adapter = new LichSuAdapter(this, lichSuList);
        rv_lichsu.setLayoutManager(new LinearLayoutManager(this));
        rv_lichsu.setAdapter(adapter);

        // 🛡️ Cấu hình OkHttpClient để chống lỗi Connection Reset
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://doan2-qhh0.onrender.com/")
                .client(okHttpClient) // Gắn client vào đây
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Ketnoi = retrofit.create(ketnoi.class);

        SharedPreferences sharedPreferences = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String taikhoan = sharedPreferences.getString("taikhoan", "");

        LayLichSuGuiXe(taikhoan);
    }

    private void LayLichSuGuiXe(String tk) {
        // 🔄 Bắt đầu tải thì hiện ProgressBar
        progressBar.setVisibility(View.VISIBLE);

        Ketnoi.LayLichSuGuiXe(tk).enqueue(new Callback<List<LichSuGuiXe>>() {
            @Override
            public void onResponse(Call<List<LichSuGuiXe>> call, Response<List<LichSuGuiXe>> response) {
                // 🔄 Xong việc (dù thành công hay lỗi HTTP) thì ẩn đi
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    lichSuList.clear();
                    lichSuList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<LichSuGuiXe>> call, Throwable t) {
                // 🔄 Lỗi kết nối cũng phải ẩn đi để tránh xoay mãi mãi
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangLichSuGuiXe.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}