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

public class ViTriAdapter extends RecyclerView.Adapter<ViTriAdapter.ViewHolder> {
    private List<ViTri> list;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener { void onItemClick(ViTri viTri); }

    public ViTriAdapter(List<ViTri> list, Context context, OnItemClickListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_vi_tri, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ViTri item = list.get(position); // Đảm bảo 'item' đúng là kiểu ViTri

        // Gọi hàm này sẽ làm tăng 'usage' trong file ViTri.java
        holder.txtTen.setText("Vị trí: " + item.getTenViTri());

        holder.txtMoTa.setText(item.getBanDo());
        holder.btnDat.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTen, txtMoTa;
        Button btnDat;
        public ViewHolder(View itemView) {
            super(itemView);
            txtTen = itemView.findViewById(R.id.txt_ten_vi_tri);
            txtMoTa = itemView.findViewById(R.id.txt_mo_ta_chi_tiet);
            btnDat = itemView.findViewById(R.id.btn_dat_ngay);
        }
    }
}