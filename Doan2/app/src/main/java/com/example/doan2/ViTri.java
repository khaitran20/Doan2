package com.example.doan2;
import com.google.gson.annotations.SerializedName;

public class ViTri {
    @SerializedName("MaViTri")
    private int MaViTri;

    @SerializedName("TenViTri")
    private String TenViTri;

    @SerializedName("MaKhuVuc")
    private int MaKhuVuc;

    @SerializedName("ViTri")
    private String BanDo;

    // Xóa hoặc comment biến Gia nếu bạn không thêm cột vào DB
    // private double Gia;

    public int getMaViTri() { return MaViTri; }
    public String getTenViTri() { return TenViTri; }
    public int getMaKhuVuc() { return MaKhuVuc; }
    public String getBanDo() { return BanDo; }
}