package com.example.hanoi_local_hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Thêm thư viện Glide trong build.gradle
// implementation 'com.github.bumptech.glide:glide:4.16.0'

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private Context context;
    private List<ServiceItem> serviceList;
    private OnServiceClickListener listener;

    // Interface callback khi click
    public interface OnServiceClickListener {
        void onServiceClick(ServiceItem service);
    }

    // Constructor có thêm listener
    public ServiceAdapter(Context context, List<ServiceItem> serviceList, OnServiceClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageAvatar;
        TextView textTitle, textPrice, textRating, textContact;

        public ViewHolder(View itemView) {
            super(itemView);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
            textTitle = itemView.findViewById(R.id.textTitle);
            textPrice = itemView.findViewById(R.id.textPrice);
            textRating = itemView.findViewById(R.id.textRating);
            textContact = itemView.findViewById(R.id.textContact);
        }
    }

    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ServiceAdapter.ViewHolder holder, int position) {
        ServiceItem service = serviceList.get(position);

        // Load ảnh từ url Firestore (nếu có ảnh)
        if (service.getPortfolioImages() != null && !service.getPortfolioImages().isEmpty()) {
            Glide.with(context)
                    .load(service.getPortfolioImages().get(0))
                    .placeholder(R.drawable.image) // ảnh mặc định nếu url null
                    .into(holder.imageAvatar);
        } else {
            holder.imageAvatar.setImageResource(R.drawable.image); // fallback nếu không có ảnh
        }

        // Set các trường hiển thị
        holder.textTitle.setText(service.getTitle());
        holder.textPrice.setText(service.getPricingInfo());
        holder.textRating.setText("★ " + service.getAverageRating());
        holder.textContact.setText("Đã có " + service.getReviewCount() + " người đánh giá");

        // Bắt sự kiện click item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onServiceClick(service);
        });
    }

    @Override
    public int getItemCount() {
        return serviceList != null ? serviceList.size() : 0;
    }

    // Hàm cập nhật lại danh sách
    public void updateData(List<ServiceItem> newList) {
        this.serviceList = newList;
        notifyDataSetChanged();
    }
}
