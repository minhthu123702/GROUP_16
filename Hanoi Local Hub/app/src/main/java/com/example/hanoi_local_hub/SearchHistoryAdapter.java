package com.example.hanoi_local_hub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder> {

    // Interface lắng nghe sự kiện xóa hoặc chọn từ khóa lịch sử
    public interface OnDeleteClickListener {
        void onDelete(int position);
        void onKeywordClick(int position);
    }

    private List<String> list;
    private OnDeleteClickListener listener;
    private boolean showAll = false;

    public SearchHistoryAdapter(List<String> list, OnDeleteClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    // Cho phép set chế độ hiển thị full/thu gọn
    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        // Nếu showAll thì trả về full, nếu không thì chỉ trả về tối đa 3 item
        return showAll ? list.size() : Math.min(list.size(), 3);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate layout dòng lịch sử tìm kiếm
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtKeyword.setText(list.get(position));

        // Sự kiện xóa (nút X)
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(holder.getAdapterPosition());
        });

        // Sự kiện click vào dòng keyword
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onKeywordClick(holder.getAdapterPosition());
        });
    }

    // ViewHolder ánh xạ đến từng thành phần trong layout item_search_history.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtKeyword;
        ImageView btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtKeyword = itemView.findViewById(R.id.txtKeyword);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
