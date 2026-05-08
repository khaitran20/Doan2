package com.example.doan2;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.CompoundBarcodeView;

import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrangGuiXe extends AppCompatActivity {
    private CompoundBarcodeView barcodeView;
    private ProgressBar progressBar;
    private String userAccount;
    private int maXeSelected;
    private static final int CAMERA_PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_gui_xe);

        barcodeView = findViewById(R.id.barcode_scanner);
        progressBar = findViewById(R.id.progressBar);
        ImageButton btnBack = findViewById(R.id.btn_back);

        // Lấy thông tin từ SharedPreferences
        SharedPreferences prefUser = getSharedPreferences("nguoidung", MODE_PRIVATE);
        userAccount = prefUser.getString("taikhoan", "");
        SharedPreferences prefXe = getSharedPreferences("KHACH_HANG_DATA", MODE_PRIVATE);
        maXeSelected = prefXe.getInt("ma_xe", 0);

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, TrangChu.class));
            finish();
        });

        // KIỂM TRA QUYỀN CAMERA
        checkCameraPermission();
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            barcodeView.decodeContinuous(callback);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                barcodeView.decodeContinuous(callback);
                barcodeView.resume();
            } else {
                Toast.makeText(this, "Bạn cần cấp quyền Camera để quét mã!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private final BarcodeCallback callback = result -> {
        if (result.getText() != null) {
            barcodeView.pause(); // Tạm dừng camera khi đã nhận diện được mã
            guiLenServer(result.getText());
        }
    };

    private void guiLenServer(String qrData) {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApi().xuLyGuiXe(qrData, userAccount, maXeSelected).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body() != null) {
                        JSONObject json = new JSONObject(response.body());
                        showCustomDialog(json.getString("status"), json);
                    }
                } catch (Exception e) {
                    Toast.makeText(TrangGuiXe.this, "Lỗi xử lý dữ liệu!", Toast.LENGTH_SHORT).show();
                    barcodeView.resume();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TrangGuiXe.this, "Lỗi kết nối server!", Toast.LENGTH_SHORT).show();
                barcodeView.resume();
            }
        });
    }

    private void showCustomDialog(String status, JSONObject json) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_thong_bao_gui_xe, null);
        builder.setView(dialogView);
        builder.setCancelable(false);

        TextView txtTitle = dialogView.findViewById(R.id.txtTitle_dialog);
        TextView txtContent = dialogView.findViewById(R.id.txtContent_dialog);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm_dialog);

        AlertDialog dialog = builder.create();

        if (status.equals("IN_SUCCESS")) {
            txtTitle.setText("GỬI XE THÀNH CÔNG");
            txtTitle.setTextColor(Color.GREEN);
            txtContent.setText("Vị trí: " + json.optString("viTri") + "\nGiá: " + json.optString("gia"));
        } else if (status.equals("PAYMENT_SUCCESS")) {
            txtTitle.setText("LẤY XE THÀNH CÔNG");
            txtTitle.setTextColor(Color.BLUE);
            txtContent.setText("Tổng tiền: " + json.optString("tongTien") + " VNĐ");
        } else {
            txtTitle.setText("THÔNG BÁO");
            txtTitle.setTextColor(Color.RED);
            txtContent.setText(json.optString("detail", "Lỗi không xác định"));
        }

        btnConfirm.setOnClickListener(v -> {
            dialog.dismiss();
            barcodeView.resume(); // Quan trọng: Khởi động lại camera sau khi đóng Dialog
        });

        dialog.show();
    }

    @Override protected void onResume() { super.onResume(); barcodeView.resume(); }
    @Override protected void onPause() { super.onPause(); barcodeView.pause(); }
}