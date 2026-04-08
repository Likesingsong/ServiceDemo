package com.martin.mvvm.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.martin.mvvm.OnItemModifyListener;
import com.martin.mvvm.R;
import com.martin.mvvm.db.bean.User;

public class EditUserDialog extends Dialog {
    private EditText usernameEdit;
    private EditText passwordEdit;
    private final User user;
    private final OnItemModifyListener listener;

    public EditUserDialog(@NonNull Context context, User user, OnItemModifyListener listener) {
        super(context);
        this.user = user;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_edit_user);

        usernameEdit = findViewById(R.id.et_edit_username);
        passwordEdit = findViewById(R.id.et_edit_password);
        Button confirmButton = findViewById(R.id.btn_confirm);
        Button cancelButton = findViewById(R.id.btn_cancel);

        // 填充当前数据
        usernameEdit.setText(user.getName());
        passwordEdit.setText(user.getPassword());

        confirmButton.setOnClickListener(v -> {
            String name = usernameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            if (!name.isEmpty() && !password.isEmpty()) {
                User updateUser = new User();
                updateUser.setId(user.getId());
                updateUser.setName(name);
                updateUser.setPassword(password);
                Log.d("EditUserDialog", "update user: " + updateUser);
                if (listener != null) {
                    listener.modify(updateUser);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dismiss());
    }
}
