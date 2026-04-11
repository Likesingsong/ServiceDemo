package com.martin.mvvm.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.martin.mvvm.R;
import com.martin.mvvm.callback.OnUserActionListener;
import com.martin.mvvm.db.bean.User;

/**
 * 添加用户的Dialog
 */
public class AddUserDialog extends Dialog {

    private EditText etUsername;
    private EditText etPassword;


    private final OnUserActionListener listener;


    public AddUserDialog(@NonNull Context context, OnUserActionListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_user);

        etUsername = findViewById(R.id.et_add_username);
        etPassword = findViewById(R.id.et_add_password);
        Button btnAdd = findViewById(R.id.btn_add);
        Button btnCancel = findViewById(R.id.btn_cancel_add);

        btnAdd.setOnClickListener(v -> {
            if (etUsername.getText() != null && etPassword.getText() != null) {
                String name = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (!name.isEmpty() && !password.isEmpty()) {
                    User user = new User();
                    user.setName(name);
                    user.setPassword(password);
                    Log.d("AddUserDialog", "add user: " + user);
                    if (listener != null) {
                        listener.onAction(user);
                    }
                }
            } else {
                Log.w("AddUserDialog", "input error: username: " + etUsername.getText()
                        + ", password: " + etPassword.getText());
            }
            dismiss();
        });
        btnCancel.setOnClickListener(v -> dismiss());
    }
}
