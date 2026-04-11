package com.martin.mvvm.ui.adpter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.martin.mvvm.callback.OnUserActionListener;
import com.martin.mvvm.R;
import com.martin.mvvm.db.bean.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users = new ArrayList<>();
    private OnUserActionListener deleteListener;
    private OnUserActionListener modifyListener;

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.nameTv.setText(currentUser.getName());
        holder.passwordTv.setText(currentUser.getPassword());
        holder.deleteBtn.setOnClickListener(v -> {
            deleteListener.onAction(currentUser);
        });
        holder.modifyBtn.setOnClickListener(v -> {
            modifyListener.onAction(currentUser);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setItemDeleteListener(OnUserActionListener listener) {
        this.deleteListener = listener;
    }

    public void setModifyListener(OnUserActionListener modifyListener) {
        this.modifyListener = modifyListener;
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        TextView passwordTv;
        Button modifyBtn;
        Button deleteBtn;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.tv_name);
            passwordTv = itemView.findViewById(R.id.tv_password);
            modifyBtn = itemView.findViewById(R.id.btn_modify);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
        }
    }
}
