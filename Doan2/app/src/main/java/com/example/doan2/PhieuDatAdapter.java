package com.example.doan2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PhieuDatAdapter extends RecyclerView.Adapter<PhieuDatAdapter.PhieuViewHolder> {

    private List<PhieuDat> dsPhieu;
    private Context context;
    private OnDeleteClickListener deleteListener;

    // Interface để xử lý sự kiện xóa từ Activity
    public interface OnDeleteClickListener {
        void onDeleteClick(PhieuDat phieu);
    }

    public PhieuDatAdapter(List<PhieuDat> dsPhieu, Context context, OnDeleteClickListener deleteListener) {
        this.dsPhieu = dsPhieu;
        this.context = context;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public PhieuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieu_dat, parent, false);
        return new PhieuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhieuViewHolder holder, int position) {
        PhieuDat phieu = dsPhieu.get(position);

        // Đổ dữ liệu vào các TextView
        holder.tvTenXe.setText("Tên xe: " + phieu.getTenXe());
        holder.tvLoaiXe.setText("Loại xe: " + phieu.getLoaiXe());
        holder.tvViTri.setText("Vị trí đậu: " + phieu.getTenViTri());
        holder.tvBanDo.setText("Bản đồ: " + phieu.getBanDo());
        holder.tvThoiGian.setText("Thời gian đặt: " + phieu.getThoiGianDat());

        // Xử lý nút xóa
        holder.btnXoa.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteClick(phieu);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dsPhieu != null ? dsPhieu.size() : 0;
    }

    public static class PhieuViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenXe, tvLoaiXe, tvViTri, tvBanDo, tvThoiGian;
        Button btnXoa;

        public PhieuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenXe = itemView.findViewById(R.id.item_ten_xe);
            tvLoaiXe = itemView.findViewById(R.id.item_loai_xe);
            tvViTri = itemView.findViewById(R.id.item_vi_tri);
            tvBanDo = itemView.findViewById(R.id.item_ban_do);
            tvThoiGian = itemView.findViewById(R.id.item_thoi_gian);
            btnXoa = itemView.findViewById(R.id.btn_xoa_phieu);
        }
    }
}