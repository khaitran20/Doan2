package com.example.doan2;

public class XeDangGui {
    private String TenViTri, ChiDan, TenXe, BienSo, TenKhuVuc;
    private int SoTienTamTinh;

    public XeDangGui(String bienSo, String chiDan, int soTienTamTinh, String tenKhuVuc, String tenViTri, String tenXe) {
        BienSo = bienSo;
        ChiDan = chiDan;
        SoTienTamTinh = soTienTamTinh;
        TenKhuVuc = tenKhuVuc;
        TenViTri = tenViTri;
        TenXe = tenXe;
    }

    public String getBienSo() {
        return BienSo;
    }

    public void setBienSo(String bienSo) {
        BienSo = bienSo;
    }

    public String getChiDan() {
        return ChiDan;
    }

    public void setChiDan(String chiDan) {
        ChiDan = chiDan;
    }

    public int getSoTienTamTinh() {
        return SoTienTamTinh;
    }

    public void setSoTienTamTinh(int soTienTamTinh) {
        SoTienTamTinh = soTienTamTinh;
    }

    public String getTenKhuVuc() {
        return TenKhuVuc;
    }

    public void setTenKhuVuc(String tenKhuVuc) {
        TenKhuVuc = tenKhuVuc;
    }

    public String getTenViTri() {
        return TenViTri;
    }

    public void setTenViTri(String tenViTri) {
        TenViTri = tenViTri;
    }

    public String getTenXe() {
        return TenXe;
    }

    public void setTenXe(String tenXe) {
        TenXe = tenXe;
    }
}