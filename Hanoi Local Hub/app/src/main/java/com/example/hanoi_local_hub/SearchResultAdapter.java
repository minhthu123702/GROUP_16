package com.example.hanoi_local_hub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ServiceItem item);
    }

    private List<ServiceItem> list;
    private OnItemClickListener listener;

    public SearchResultAdapter(List<ServiceItem> list, OnItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceItem item = list.get(position);

        // Load ảnh từ URL Firestore (nếu có)
        if (item.getPortfolioImages() != null && !item.getPortfolioImages().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getPortfolioImages().get(0))
                    .placeholder(R.drawable.image)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(R.drawable.image);
        }

        holder.txtTitle.setText(item.getTitle());
        holder.txtPrice.setText(item.getPricingInfo());

        // Hiện rating dạng "4.9 ★"
        holder.txtRating.setText(item.getAverageRating() + " ★");

        // Hiển thị: "20 đánh giá"
        String reviewText = item.getReviewCount() > 0 ? (item.getReviewCount() + " đánh giá") : "";
        holder.txtContact.setText(reviewText);

        // Click event
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    // Hàm cập nhật lại dữ liệu cho adapter
    public void updateList(List<ServiceItem> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView txtTitle, txtPrice, txtRating, txtContact;

        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtContact = itemView.findViewById(R.id.txtContact);
        }
    }
}
