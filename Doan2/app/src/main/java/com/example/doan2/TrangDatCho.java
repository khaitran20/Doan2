package com.example.doan2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangDatCho extends AppCompatActivity {
    RecyclerView rv;
    ketnoi Ketnoi;
    ConstraintLayout layout_da_chon;
    View layout_chua_chon;
    TextView txtTenXe, txtBienSo, txtKhuVuc, txtGia;
    ImageButton btnBack, btnDSDatCho;
    ProgressBar progressBar; // 🔄 Thêm ProgressBar

    int maKhachHangHienTai = 0;
    private boolean isProcessing = false; // 🔒 Cờ ngăn chặn thao tác trùng lặp
    private static final String TAG = "DEBUG_DAT_CHO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_dat_cho);

        anhXaView();
        khoiTaoRetrofit();
        kiemTraTrangThaiNguoiDung();

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, TrangChu.class));
            finish();
        });

        btnDSDatCho.setOnClickListener(v -> {
            Intent intent = new Intent(TrangDatCho.this, TrangDSDatCho.class);
            startActivity(intent);
        });
    }

    private void anhXaView() {
        rv = findViewById(R.id.recycler_vi_tri_trong);
        layout_da_chon = findViewById(R.id.layout_da_chon_xe);
        layout_chua_chon = findViewById(R.id.layout_chua_chon);
        txtTenXe = findViewById(R.id.txt_ten_xe_chon);
        txtBienSo = findViewById(R.id.txt_bien_so_chon);
        txtKhuVuc = findViewById(R.id.txt_khu_vuc_goi_y); // Đã sửa ID cho khớp
        txtGia = findViewById(R.id.txt_gia_tien_hien_thi);
        btnBack = findViewById(R.id.btn_back_dat_cho);
        btnDSDatCho = findViewById(R.id.btn_danh_sach_dat_cho);
        progressBar = findViewById(R.id.progressBar); // Thêm ánh xạ

        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    private void khoiTaoRetrofit() {
        // Thêm OkHttpClient để tăng độ ổn định, tránh lỗi Connection Reset
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Ketnoi = new Retrofit.Builder()
                .baseUrl("https://baigiuxe.elementfx.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ketnoi.class);
    }

    private void kiemTraTrangThaiNguoiDung() {
        progressBar.setVisibility(View.VISIBLE);
        SharedPreferences shared = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String tk = shared.getString("taikhoan", "");

        Ketnoi.LayThongTinNguoiDung(tk).enqueue(new Callback<TTNguoiDung>() {
            @Override
            public void onResponse(Call<TTNguoiDung> call, Response<TTNguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    maKhachHangHienTai = response.body().getMaKhachHang();
                    layDuLieuXeDaChon();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.e(TAG, "Lỗi lấy thông tin người dùng: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<TTNguoiDung> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangDatCho.this, "Lỗi kết nối máy chủ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void layDuLieuXeDaChon() {
        SharedPreferences shared = getSharedPreferences("KHACH_HANG_DATA", MODE_PRIVATE);
        String tenXe = shared.getString("ten_xe", "");
        String loaiXe = shared.getString("loai_xe", "");
        String bS = shared.getString("bien_so", "");

        if (tenXe.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            layout_da_chon.setVisibility(View.GONE);
            layout_chua_chon.setVisibility(View.VISIBLE);
        } else {
            layout_da_chon.setVisibility(View.VISIBLE);
            layout_chua_chon.setVisibility(View.GONE);
            setupUI(tenXe, loaiXe, bS);
        }
    }

    private void setupUI(String tenXe, String loaiXe, String bS) {
        txtTenXe.setText("Tên xe: " + tenXe);
        txtBienSo.setText("Biển số: " + bS);

        int maKhu;
        if (loaiXe.contains("Xe oto")) {
            maKhu = 2;
            txtKhuVuc.setText("Khu vực gợi ý: Khu B");
            txtGia.setText("Giá: 20.000đ/h");
        } else if (loaiXe.contains("Xe đạp")) {
            maKhu = 3;
            txtKhuVuc.setText("Khu vực gợi ý: Khu C");
            txtGia.setText("Giá: 3.000đ/h");
        } else {
            maKhu = 1;
            txtKhuVuc.setText("Khu vực gợi ý: Khu A");
            txtGia.setText("Giá: 5.000đ/h");
        }
        loadDanhSachViTriTrong(maKhu);
    }

    private void loadDanhSachViTriTrong(int maKhu) {
        Ketnoi.getViTriTrong("get_spots", maKhu).enqueue(new Callback<List<ViTri>>() {
            @Override
            public void onResponse(Call<List<ViTri>> call, Response<List<ViTri>> res) {
                progressBar.setVisibility(View.GONE); // Tắt khi tải xong danh sách
                if (res.isSuccessful() && res.body() != null) {
                    rv.setAdapter(new ViTriAdapter(res.body(), TrangDatCho.this, item -> {
                        if (isProcessing) return; // Chống spam click

                        SharedPreferences carPrefs = getSharedPreferences("KHACH_HANG_DATA", MODE_PRIVATE);
                        int maXe = carPrefs.getInt("ma_xe", 0);

                        new AlertDialog.Builder(TrangDatCho.this)
                                .setTitle("Xác nhận")
                                .setMessage("Bạn muốn đặt chỗ tại vị trí " + item.getTenViTri() + "?")
                                .setPositiveButton("Đặt ngay", (dialog, which) -> {
                                    tienHanhGhiPhieuDatMoi(item.getMaViTri(), maXe);
                                })
                                .setNegativeButton("Hủy", null)
                                .show();
                    }));
                }
            }
            @Override public void onFailure(Call<List<ViTri>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Lỗi tải vị trí trống: " + t.getMessage());
            }
        });
    }

    private void tienHanhGhiPhieuDatMoi(int maViTri, int maXe) {
        isProcessing = true;
        progressBar.setVisibility(View.VISIBLE);

        Ketnoi.datChoXe("book_spot", maViTri, maKhachHangHienTai, maXe).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                isProcessing = false;
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body();
                    if (result.equals("success")) {
                        Toast.makeText(TrangDatCho.this, "Đặt chỗ thành công!", Toast.LENGTH_SHORT).show();
                        // Tải lại danh sách sau khi đặt thành công
                        layDuLieuXeDaChon();
                    } else if (result.equals("car_already_in_parking")) {
                        showInfoDialog("Xe này hiện đã có trong bãi hoặc đã đặt vị trí khác rồi.");
                    } else if (result.equals("spot_taken")) {
                        Toast.makeText(TrangDatCho.this, "Vị trí này vừa có người đặt!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(TrangDatCho.this, "Lỗi: " + result, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                isProcessing = false;
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangDatCho.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInfoDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage(message)
                .setPositiveButton("Đồng ý", null)
                .show();
    }
}