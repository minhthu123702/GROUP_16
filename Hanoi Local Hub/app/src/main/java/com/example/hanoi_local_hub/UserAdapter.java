package com.example.hanoi_local_hub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.CookieHandler;
import java.util.List;

// UserAdapter.java
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private OnUserClickListener onUserClickListener;

    // Interface để xử lý sự kiện (ví dụ: xóa user)
    public interface OnUserClickListener {
        void onDeleteClick(User user, int position);
    }

    public UserAdapter(List<User> users, OnUserClickListener listener) {
        this.users = users;
        this.onUserClickListener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.txtName.setText(user.getName());
        holder.txtCode.setText("Mã: " + user.getCode());

        // Gán avatar cho user
        holder.imgAvatar.setImageResource(user.getAvatarResId());

        // Gán trạng thái: online (green_dot), offline (red_dot)
        holder.imgStatus.setImageResource(
                user.isOnline() ? R.drawable.green_dot : R.drawable.red_dot
        );
        holder.btnDisplay.setOnClickListener(v -> {
            Context context = null;
            Intent intent = new Intent(context, CustomerProfileActivity.class);
            CookieHandler userList = null;
            intent.putExtra("user", userList.get(position));
            context.startActivity(intent);
        });
        // Xử lý nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            if (onUserClickListener != null) {
                onUserClickListener.onDeleteClick(user, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Xóa user khỏi danh sách
    public void removeUser(int position) {
        users.remove(position);
        notifyItemRemoved(position);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, imgStatus;
        TextView txtName, txtCode;
        ImageButton btnDelete;
        ImageButton btnDisplay;
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
