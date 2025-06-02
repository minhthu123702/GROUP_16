package com.example.hanoi_local_hub;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private Context context;
    private List<Service> serviceList;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
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
        Service service = serviceList.get(position);
        holder.imageAvatar.setImageResource(service.imageResId);
        holder.textTitle.setText(service.title);
        holder.textPrice.setText(service.price);
        holder.textRating.setText("★ " + service.rating);
        holder.textContact.setText("Đã liên lạc " + service.contact);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }
}

