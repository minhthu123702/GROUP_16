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
// import com.bumptech.glide.Glide; // Đã xóa thư viện không còn dùng
import com.example.hanoi_local_hub.R;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<ServiceModel> serviceList;
    private OnItemClickListener listener;

    // Interface vẫn giữ nguyên với onItemViewClick
    public interface OnItemClickListener {
        void onEditClick(ServiceModel service);
        void onHideClick(ServiceModel service);
        void onDeleteClick(ServiceModel service);
        void onItemViewClick(ServiceModel service);
    }

    public ServiceAdapter(Context context, List<ServiceModel> serviceList, OnItemClickListener listener) {
        this.context = context;
        this.serviceList = serviceList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.services_item, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        ServiceModel currentService = serviceList.get(position);

        // Chỉ gán dữ liệu text, không còn xử lý ảnh
        holder.tvServiceTitle.setText(currentService.getTitle());
        holder.tvServicePrice.setText(currentService.getPricingInfo());
        holder.tvServiceRating.setText(String.format("%.1f", currentService.getAverageRating()));
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        // Đã xóa ImageView imgServiceThumbnail
        TextView tvServiceTitle, tvServiceRating, tvServicePrice;
        Button btnDelete, btnHide, btnEdit;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);

            // Đã xóa findViewById cho ImageView
            tvServiceTitle = itemView.findViewById(R.id.tvServiceTitle);
            tvServiceRating = itemView.findViewById(R.id.tvServiceRating);
            tvServicePrice = itemView.findViewById(R.id.tvServicePrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnHide = itemView.findViewById(R.id.btnHide);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            // Sự kiện click cho toàn bộ thẻ vẫn được giữ lại
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemViewClick(serviceList.get(position));
                }
            });


            // Các sự kiện click cho các nút bên trong giữ nguyên
            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(serviceList.get(position));
                }
            });

            btnHide.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onHideClick(serviceList.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(serviceList.get(position));
                }
            });
        }
    }
}