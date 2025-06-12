package com.example.hanoi_local_hub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<ServiceItem> list;

    public SearchResultAdapter(List<ServiceItem> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ServiceItem item = list.get(position);
        holder.imgAvatar.setImageResource(item.getImageResId());
        holder.txtTitle.setText(item.getTitle());
        holder.txtPrice.setText(item.getPrice());
        holder.txtRating.setText(item.getRating() + " ★");
        holder.txtContact.setText("Đã liên lạc " + item.getContact());
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
