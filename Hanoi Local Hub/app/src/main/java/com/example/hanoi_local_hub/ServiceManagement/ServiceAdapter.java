package com.example.hanoi_local_hub.ServiceManagement; // Thay bằng package của bạn

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Thư viện tải ảnh, bạn cần thêm nó vào build.gradle
import com.example.hanoi_local_hub.R;
import com.example.hanoi_local_hub.ServiceManagement.ServiceModel;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<ServiceModel> serviceList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(ServiceModel service);
        void onHideClick(ServiceModel service);
        void onDeleteClick(ServiceModel service);
    }

    public ServiceAdapter(Context context, List<ServiceModel> serviceList, OnItemClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.services_item, parent, false); // Sử dụng layout của bạn
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel currentService = serviceList.get(position);

        holder.tvServiceTitle.setText(currentService.getTitle());
        holder.tvServicePrice.setText(currentService.getPricingInfo());
        holder.tvServiceRating.setText(String.format("%.1f", currentService.getAverageRating()));

        // Tải ảnh thumbnail (ảnh đầu tiên trong portfolio)
        if (currentService.getPortfolioImages() != null && !currentService.getPortfolioImages().isEmpty()) {
            Glide.with(context)
                    .load(currentService.getPortfolioImages().get(0))
                    .placeholder(R.drawable.ic_circle) // Ảnh mặc định trong lúc tải
                    .error(R.drawable.ic_circle)     // Ảnh mặc định nếu tải lỗi
                    .into(holder.imgServiceThumbnail);
        } else {
            holder.imgServiceThumbnail.setImageResource(R.drawable.ic_circle);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        ImageView imgServiceThumbnail;
        TextView tvServiceTitle, tvServiceRating, tvServicePrice;
        Button btnDelete, btnHide, btnEdit;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            imgServiceThumbnail = itemView.findViewById(R.id.imgServiceThumbnail);
            tvServiceTitle = itemView.findViewById(R.id.tvServiceTitle);
            tvServiceRating = itemView.findViewById(R.id.tvServiceRating);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnHide = itemView.findViewById(R.id.btnHide);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            // Gán sự kiện click, truyền cả object service về cho Activity xử lý
            btnEdit.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onEditClick(serviceList.get(getAdapterPosition()));
                }
            });

            btnHide.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onHideClick(serviceList.get(getAdapterPosition()));
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(serviceList.get(getAdapterPosition()));
                }
            });
        }
    }
}