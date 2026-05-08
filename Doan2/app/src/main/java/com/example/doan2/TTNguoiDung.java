package com.example.doan2;

public class TTNguoiDung {
    private int MaKhachHang;
    private int MaTaiKhoan;
    private String MatKhau;
    private String TenKhachHang;
    private String SoDienThoai;

    private String NgaySinh;
    private String DiaChi;

    private Double SoDu;

    // Trong file TTNguoiDung.java, hãy thêm Constructor này:
    public TTNguoiDung() {
        // Để trống - Đây là Constructor mặc định cho GSON
    }
    public TTNguoiDung(int maKhachHang, int maTaiKhoan, String soDienThoai, String tenKhachHang, String ngaySinh , String diaChi , String matKhau ,Double soDu) {
        MaKhachHang = maKhachHang;
        MaTaiKhoan = maTaiKhoan;
        SoDienThoai = soDienThoai;
        TenKhachHang = tenKhachHang;
        NgaySinh = ngaySinh;
        DiaChi = diaChi;
        MatKhau = matKhau;
        this.SoDu = soDu;
    }
    public Double getSoDu() {
        return SoDu;
    }
    public void setSoDu(Double soDu) {
        SoDu = soDu;
    }
    public String getMatKhau() {
        return MatKhau;
    }
    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }
    public String getNgaySinh() {
        return NgaySinh;
    }
    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }
    public String getDiaChi() {
        return DiaChi;
    }
    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }
    public int getMaKhachHang() {
        return MaKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        MaKhachHang = maKhachHang;
    }

    public int getMaTaiKhoan() {
        return MaTaiKhoan;
    }

    public void setMaTaiKhoan(int maTaiKhoan) {
        MaTaiKhoan = maTaiKhoan;
    }

    public String getSoDienThoai() {
        return SoDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        SoDienThoai = soDienThoai;
    }

    public String getTenKhachHang() {
        return TenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        TenKhachHang = tenKhachHang;
    }

}
