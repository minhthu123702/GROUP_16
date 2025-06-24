package com.example.hanoi_local_hub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import com.bumptech.glide.Glide;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> users;
    private OnUserClickListener onUserClickListener;

    public interface OnUserClickListener {
        void onUserClick(User user);                // Xem chi tiết
        void onDeleteClick(User user, int position); // Xoá user
    }

    public UserAdapter(Context context, List<User> users, OnUserClickListener listener) {
        this.context = context;
        this.users = users;
        this.onUserClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.txtName.setText(user.getName());
        holder.txtCode.setText("Mã: " + user.getCode());

        // Load avatar từ URL nếu có, không thì dùng avatarResId mặc định
        if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
            Glide.with(context)
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.avatar1)
                    .error(R.drawable.avatar1)
                    .into(holder.imgAvatar);
        } else {
            holder.imgAvatar.setImageResource(user.getAvatarResId());
        }

        holder.imgStatus.setImageResource(user.isOnline() ? R.drawable.green_dot : R.drawable.red_dot);

        holder.btnDisplay.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onUserClick(user);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onDeleteClick(user, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void removeUser(int position) {
        users.remove(position);
        notifyItemRemoved(position);
    }

    public void setFilteredList(List<User> filteredList) {
        this.users = filteredList;
        notifyDataSetChanged();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imgStatus;
        TextView txtName, txtCode;
        ImageButton btnDelete, btnDisplay;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            txtName = itemView.findViewById(R.id.txtName);
            txtCode = itemView.findViewById(R.id.txtCode);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDisplay = itemView.findViewById(R.id.btnDisplay);
        }
    }
}
