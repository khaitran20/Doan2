package com.example.doan2;

import com.google.gson.annotations.SerializedName;

public class LichSuGuiXe {
    @SerializedName("BienSo")
    private String BienSo;

    @SerializedName("LoaiXe")
    private String LoaiXe;

    @SerializedName("ThoiGianVao")
    private String ThoiGianVao;

    @SerializedName("ThoiGianRa")
    private String ThoiGianRa;

    @SerializedName("ThanhTien")
    private int ThanhTien;

    public LichSuGuiXe(String bienSo, String loaiXe, int thanhTien, String thoiGianRa, String thoiGianVao) {
        BienSo = bienSo;
        LoaiXe = loaiXe;
        ThanhTien = thanhTien;
        ThoiGianRa = thoiGianRa;
        ThoiGianVao = thoiGianVao;
    }

    public String getBienSo() {
        return BienSo;
    }

    public void setBienSo(String bienSo) {
        BienSo = bienSo;
    }

    public String getLoaiXe() {
        return LoaiXe;
    }

    public void setLoaiXe(String loaiXe) {
        LoaiXe = loaiXe;
    }

    public int getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(int thanhTien) {
        ThanhTien = thanhTien;
    }

    public String getThoiGianRa() {
        return ThoiGianRa;
    }

    public void setThoiGianRa(String thoiGianRa) {
        ThoiGianRa = thoiGianRa;
    }

    public String getThoiGianVao() {
        return ThoiGianVao;
    }

    public void setThoiGianVao(String thoiGianVao) {
        ThoiGianVao = thoiGianVao;
    }
}