package com.example.doan2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class XeAdapter extends RecyclerView.Adapter<XeAdapter.ViewHolder> {
    private List<Xe> list;
    private Context context;
    private OnXeClickListener listener;

    // Interface để xử lý sự kiện từ Activity
    public interface OnXeClickListener {
        void onItemSelected(Xe xe); // Click chọn xe để đặt chỗ
        void onItemDelete(Xe xe);  // Click nút thùng rác để xóa
    }

    public XeAdapter(List<Xe> list, Context context, OnXeClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_xe, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Xe xe = list.get(position);
        holder.txtTen.setText(xe.getTenXe());
        holder.txtLoai.setText("Loại: " + xe.getLoaiXe());
        holder.txtBienSo.setText(xe.getBienSo());

        // Sự kiện click vào cả item để chọn xe
        holder.itemView.setOnClickListener(v -> listener.onItemSelected(xe));

        // Sự kiện click vào nút xóa
        holder.btnXoa.setOnClickListener(v -> listener.onItemDelete(xe));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtLoai, txtBienSo;
        ImageButton btnXoa; // Thêm nút xóa

        public ViewHolder(View iv) {
            super(iv);
            txtTen = iv.findViewById(R.id.txt_ten_xe_item);
            txtLoai = iv.findViewById(R.id.txt_loai_xe_item);
            txtBienSo = iv.findViewById(R.id.txt_bien_so_item);
            btnXoa = iv.findViewById(R.id.btn_xoa_xe); // ID từ item_xe.xml
        }
    }
}