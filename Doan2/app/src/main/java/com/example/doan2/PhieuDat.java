package com.example.doan2;

import com.google.gson.annotations.SerializedName;

public class PhieuDat {
    // Sử dụng @SerializedName để đảm bảo khớp với tên cột trong CSDL/JSON trả về
    @SerializedName("MaPhieuDat")
    private int maPhieuDat;

    @SerializedName("MaViTri")
    private int maViTri;

    @SerializedName("TenXe")
    private String tenXe;

    @SerializedName("LoaiXe")
    private String loaiXe;

    @SerializedName("TenViTri")
    private String tenViTri;

    @SerializedName("BanDo") // Đây là cột ViTri trong bảng vitridau mà ta đặt alias là BanDo
    private String banDo;

    @SerializedName("ThoiGianDat")
    private String thoiGianDat;

    @SerializedName("TenKhu") // Khớp với alias hoặc tên cột trong SQL JOIN
    private String tenKhu;

    // Getter và Setter cho TenKhu
    public String getTenKhu() {
        return tenKhu;
    }

    public void setTenKhu(String tenKhu) {
        this.tenKhu = tenKhu;
    }

    // Constructor không đối số (cần thiết cho một số thư viện mapping)
    public PhieuDat() {
    }

    // Getter và Setter
    public int getMaPhieuDat() {
        return maPhieuDat;
    }

    public void setMaPhieuDat(int maPhieuDat) {
        this.maPhieuDat = maPhieuDat;
    }

    public int getMaViTri() {
        return maViTri;
    }

    public void setMaViTri(int maViTri) {
        this.maViTri = maViTri;
    }

    public String getTenXe() {
        return tenXe;
    }

    public void setTenXe(String tenXe) {
        this.tenXe = tenXe;
    }

    public String getLoaiXe() {
        return loaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        this.loaiXe = loaiXe;
    }

    public String getTenViTri() {
        return tenViTri;
    }

    public void setTenViTri(String tenViTri) {
        this.tenViTri = tenViTri;
    }

    public String getBanDo() {
        return banDo;
    }

    public void setBanDo(String banDo) {
        this.banDo = banDo;
    }

    public String getThoiGianDat() {
        return thoiGianDat;
    }

    public void setThoiGianDat(String thoiGianDat) {
        this.thoiGianDat = thoiGianDat;
    }
}