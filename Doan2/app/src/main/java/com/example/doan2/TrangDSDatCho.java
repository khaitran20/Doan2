package com.example.doan2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View; // Thêm View để dùng View.VISIBLE/GONE
import android.widget.ImageButton;
import android.widget.ProgressBar; // 🔄 Thêm ProgressBar
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrangDSDatCho extends AppCompatActivity {

    private RecyclerView rvPhieuDat;
    private PhieuDatAdapter adapter;
    private List<PhieuDat> dsPhieu = new ArrayList<>();
    private ketnoi Ketnoi;
    private ImageButton btnBack;
    private ProgressBar progressBar; // 🔄 Khai báo biến
    private int maKhachHangHienTai = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_dsdat_cho);

        anhXa();
        khoiTaoRetrofit();
        layMaKhachHang();

        btnBack.setOnClickListener(v -> finish());
    }

    private void anhXa() {
        rvPhieuDat = findViewById(R.id.rv_phieu_dat);
        btnBack = findViewById(R.id.btn_back_ds);
        progressBar = findViewById(R.id.progressBar); // 🔄 Ánh xạ từ XML
        rvPhieuDat.setLayoutManager(new LinearLayoutManager(this));
    }

    private void khoiTaoRetrofit() {
        Ketnoi = new Retrofit.Builder()
                .baseUrl("https://doan2-qhh0.onrender.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ketnoi.class);
    }

    private void layMaKhachHang() {
        // 🔄 Hiện vòng xoay khi bắt đầu lấy dữ liệu
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences shared = getSharedPreferences("nguoidung", MODE_PRIVATE);
        String tk = shared.getString("taikhoan", "");

        Ketnoi.LayThongTinNguoiDung(tk).enqueue(new Callback<TTNguoiDung>() {
            @Override
            public void onResponse(Call<TTNguoiDung> call, Response<TTNguoiDung> response) {
                if (response.isSuccessful() && response.body() != null) {
                    maKhachHangHienTai = response.body().getMaKhachHang();
                    taiDanhSachPhieu();
                } else {
                    // 🔄 Ẩn nếu không tìm thấy dữ liệu
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TTNguoiDung> call, Throwable t) {
                // 🔄 Ẩn khi lỗi kết nối
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangDSDatCho.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void taiDanhSachPhieu() {
        // 🔄 Đảm bảo ProgressBar vẫn hiện khi đang tải danh sách
        progressBar.setVisibility(View.VISIBLE);

        Ketnoi.getDSPhieuDat("get_list_phieu", maKhachHangHienTai).enqueue(new Callback<List<PhieuDat>>() {
            @Override
            public void onResponse(Call<List<PhieuDat>> call, Response<List<PhieuDat>> response) {
                // 🔄 Ẩn vòng xoay sau khi nhận được danh sách
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    dsPhieu = response.body();
                    adapter = new PhieuDatAdapter(dsPhieu, TrangDSDatCho.this, phieu -> {
                        xoaPhieuDatCho(phieu);
                    });
                    rvPhieuDat.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<PhieuDat>> call, Throwable t) {
                // 🔄 Ẩn vòng xoay khi lỗi
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangDSDatCho.this, "Lỗi tải danh sách phiếu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xoaPhieuDatCho(PhieuDat phieu) {
        // 🔄 Hiện vòng xoay khi đang thực hiện xóa
        progressBar.setVisibility(View.VISIBLE);

        Ketnoi.xoaPhieuDat("delete_phieu", phieu.getMaPhieuDat(), phieu.getMaViTri()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // 🔄 Lưu ý: ProgressBar sẽ được hàm taiDanhSachPhieu() quản lý sau khi gọi lại ở dưới
                if (response.isSuccessful() && "success".equals(response.body())) {
                    Toast.makeText(TrangDSDatCho.this, "Đã xóa phiếu đặt chỗ!", Toast.LENGTH_SHORT).show();
                    taiDanhSachPhieu();
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(TrangDSDatCho.this, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangDSDatCho.this, "Lỗi kết nối khi xóa phiếu", Toast.LENGTH_SHORT).show();
            }
        });
    }
}