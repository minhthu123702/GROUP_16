package com.example.hanoi_local_hub.ServiceManagement;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.hanoi_local_hub.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteConfirmationDialog extends DialogFragment {

    private static final String ARG_SERVICE_ID = "serviceId";
    private String serviceId;
    private OnDeleteListener listener;

    // Interface để giao tiếp ngược lại với Activity
    public interface OnDeleteListener {
        void onDeleteSuccess();
    }

    // Phương thức factory để tạo fragment và truyền dữ liệu một cách an toàn
    public static DeleteConfirmationDialog newInstance(String serviceId) {
        DeleteConfirmationDialog fragment = new DeleteConfirmationDialog();
        Bundle args = new Bundle();
        args.putString(ARG_SERVICE_ID, serviceId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Gán listener từ Activity gọi nó
        if (context instanceof OnDeleteListener) {
            listener = (OnDeleteListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnDeleteListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            serviceId = getArguments().getString(ARG_SERVICE_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng file layout activity_delete_comfired.xml của bạn
        return inflater.inflate(R.layout.activity_delete_comfired, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Ánh xạ các nút từ view của fragment
        ImageView btnClose = view.findViewById(R.id.btn_close);
        Button btnAgree = view.findViewById(R.id.btn_agree);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        // Gán sự kiện click
        btnAgree.setOnClickListener(v -> {
            deleteServiceFromFirestore();
        });

        View.OnClickListener cancelAction = v -> dismiss(); // Đóng dialog
        btnClose.setOnClickListener(cancelAction);
        btnCancel.setOnClickListener(cancelAction);
    }

    // Làm cho dialog có các góc bo tròn giống như layout
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private void deleteServiceFromFirestore() {
        if (serviceId == null || serviceId.isEmpty()) {
            Toast.makeText(getContext(), "Lỗi: Không có ID dịch vụ.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore.getInstance().collection("services").document(serviceId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    // Xóa thành công, gọi listener để báo cho Activity
                    if (listener != null) {
                        listener.onDeleteSuccess();
                    }
                    dismiss(); // Đóng dialog
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi xóa dịch vụ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    dismiss();
                });
    }
}