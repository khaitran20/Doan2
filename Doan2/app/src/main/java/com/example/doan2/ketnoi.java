package com.example.doan2;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ketnoi {

    @FormUrlEncoded
    @POST("DangNhap.php") // Phải khớp với tên file trên x10Hosting
    Call<String> kiemTraDangNhap(
            @Field("taikhoan") String tk,
            @Field("matkhau") String mk
    );

    @FormUrlEncoded
    @POST("DangKy.php")
    Call<String> dangKy(
            @Field("TenDangNhap") String user,
            @Field("MatKhau") String pass,
            @Field("TenKhachHang") String ten,
            @Field("SoDienThoai") String sdt,
            @Field("NgaySinh") String ns,
            @Field("DiaChi") String dc
    );

    @FormUrlEncoded
    @POST("LayThongTinNguoiDung.php")
    Call<TTNguoiDung> LayThongTinNguoiDung(
            @Field("taikhoan") String tk
    );

    @FormUrlEncoded
    @POST("SuaTTNguoiDung.php")
    Call<String> SuaTTNguoiDung(
            @Field("MaKhachHang") int makh,
            @Field("MaTaiKhoan") int matk,
            @Field("TenKhachHang") String hoten,
            @Field("SoDienThoai") String sdt,
            @Field("NgaySinh") String ngaysinh,
            @Field("DiaChi") String diachi,
            @Field("MatKhau") String matkhau
    );

    @FormUrlEncoded
    @POST("LichSuGuiXe.php")
    Call<List<LichSuGuiXe>> LayLichSuGuiXe(
            @Field("taikhoan") String tk
    );


    // Truy vấn danh sách xe dựa trên tài khoản
    @FormUrlEncoded
    @POST("PhuongTien.php")
    Call<List<Xe>> layDanhSachPhuongTien(
            @Field("taikhoan") String taikhoan
    );

    @FormUrlEncoded
    @POST("XoaPhuongTien.php")
    Call<String> xoaXe(@Field("MaXe") int maXe);

    // Lấy danh sách vị trí còn trống (Trả về List thay vì String)
    @FormUrlEncoded
    @POST("DatCho.php")
    Call<List<ViTri>> getViTriTrong(
            @Field("action") String action,
            @Field("MaKhuVuc") int maKhuVuc
    );

    // Trong file ketnoi.java

    @FormUrlEncoded // THÊM DÒNG NÀY
    @POST("DatCho.php")
    Call<String> datChoXe(
            @Field("action") String action,
            @Field("MaViTri") int maViTri,
            @Field("MaKhachHang") int maKhachHang,
            @Field("MaXe") int maXe
    );

    // Trong file ketnoi.java

    // MỚI: Lấy danh sách phiếu đặt chỗ của khách hàng
    @FormUrlEncoded
    @POST("DatCho.php")
    Call<List<PhieuDat>> getDSPhieuDat(
            @Field("action") String action,
            @Field("MaKhachHang") int maKH
    );

    // MỚI: Xóa phiếu đặt chỗ
    @FormUrlEncoded
    @POST("DatCho.php")
    Call<String> xoaPhieuDat(
            @Field("action") String action,
            @Field("MaPhieu") int maPhieu,
            @Field("MaViTri") int maViTri
    );
    @FormUrlEncoded
    @POST("DatCho.php")
    Call<PhieuDat> checkCarBooked(
            @Field("action") String action,
            @Field("MaXe") int maXe
    );
    @FormUrlEncoded
    @POST("LayThongTinKhuVuc.php") // Bạn cần tạo file PHP này
    Call<KhuVuc> getChiTietKhuVuc(
            @Field("MaKhuVuc") int maKhuVuc
    );
    // Tìm đến dòng này trong file ketnoi.java
    @FormUrlEncoded
    @POST("xu_ly_guixe.php") // Sửa đúng tên file có 2 dấu gạch dưới
    Call<String> xuLyGuiXe(
            @Field("maQuet") String maQuet,
            @Field("maKhachHang") String maKhachHang,
            @Field("ma_xe") int maXe
    );
    // 2. API Nạp tiền (Sử dụng cho TrangNapTien)
    @FormUrlEncoded
    @POST("nap_tien.php")
    Call<String> napTien(
            @Field("user_id") String userId,
            @Field("amount") long amount,
            @Field("phuongthuc") String phuongThuc
    );
    @FormUrlEncoded
    @POST("ThemXe.php")
    Call<String> themXe(
            @Field("BienSo") String bienSo,
            @Field("LoaiXe") String loaiXe,
            @Field("TenXe") String tenXe,
            @Field("taikhoan") String taikhoan // Gửi tài khoản để Server tự tìm MaKhachHang
    );
    @FormUrlEncoded
    @POST("LayXeDangGui.php")
    Call<List<XeDangGui>> layXeDangGui(@Field("taikhoan") String tk);
}