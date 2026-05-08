package com.example.doan2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LichSuAdapter extends RecyclerView.Adapter<LichSuAdapter.ViewHolder> {
    private Context context;
    private List<LichSuGuiXe> list;

    public LichSuAdapter(Context context, List<LichSuGuiXe> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Đảm bảo tên file layout là item_lich_su
        View v = LayoutInflater.from(context).inflate(R.layout.item_lich_su, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichSuGuiXe ls = list.get(position);

        // Đổ dữ liệu vào các ID đã được chuẩn hóa
        holder.txt_bienso.setText(ls.getBienSo());

        String thoiGian = "Vào: " + ls.getThoiGianVao();
        if (ls.getThoiGianRa() != null && !ls.getThoiGianRa().isEmpty()) {
            thoiGian += " - Ra: " + ls.getThoiGianRa();
            holder.txt_trangthai.setText("Đã hoàn thành");
            holder.txt_trangthai.setTextColor(Color.parseColor("#4CAF50")); // Màu xanh lá
        } else {
            holder.txt_trangthai.setText("Đang trong bãi");
            holder.txt_trangthai.setTextColor(Color.parseColor("#F44336")); // Màu đỏ
        }

        holder.txt_thoigian.setText(thoiGian);
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các ID theo bản thiết kế mới
        TextView txt_bienso, txt_thoigian, txt_trangthai;

        public ViewHolder(View itemView) {
            super(itemView);
            txt_bienso = itemView.findViewById(R.id.txt_bienso);
            txt_thoigian = itemView.findViewById(R.id.txt_thoigian);
            txt_trangthai = itemView.findViewById(R.id.txt_trangthai);
        }
    }
}