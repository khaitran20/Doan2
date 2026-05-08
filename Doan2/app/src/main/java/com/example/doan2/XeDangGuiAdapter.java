package com.example.doan2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class XeDangGuiAdapter extends RecyclerView.Adapter<XeDangGuiAdapter.ViewHolder> {
    private List<XeDangGui> list;

    public XeDangGuiAdapter(List<XeDangGui> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_xe_dang_gui, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        XeDangGui item = list.get(position);
        holder.txtViTri.setText("Vị trí: " + item.getTenViTri());
        holder.txtThongTinXe.setText(item.getTenXe() + " - " + item.getBienSo());
        holder.txtChiDan.setText("Chỉ dẫn: " + item.getChiDan());

        // Định dạng tiền tệ
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        holder.txtTien.setText("Tạm tính: " + formatter.format(item.getSoTienTamTinh()) + "đ");
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtViTri, txtThongTinXe, txtChiDan, txtTien;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViTri = itemView.findViewById(R.id.txtViTri);
            txtThongTinXe = itemView.findViewById(R.id.txtThongTinXe);
            txtChiDan = itemView.findViewById(R.id.txtChiDan);
            txtTien = itemView.findViewById(R.id.txtTien);
        }
    }
}