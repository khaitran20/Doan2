package com.example.doan2;

import com.google.gson.annotations.SerializedName;

public class KhuVuc {
    @SerializedName("MaKhuVuc")
    private int maKhuVuc;
    @SerializedName("TenKhuVuc")
    private String tenKhuVuc;
    @SerializedName("MoTa")
    private String moTa;
    @SerializedName("SoLuongCho")
    private int soLuongCho;
    @SerializedName("BanDo")
    private String banDo;
    @SerializedName("Gia")
    private double gia;

    // Getters
    public int getMaKhuVuc() { return maKhuVuc; }
    public String getTenKhuVuc() { return tenKhuVuc; }
    public String getMoTa() { return moTa; }
    public int getSoLuongCho() { return soLuongCho; }
    public String getBanDo() { return banDo; }
    public double getGia() { return gia; }
}