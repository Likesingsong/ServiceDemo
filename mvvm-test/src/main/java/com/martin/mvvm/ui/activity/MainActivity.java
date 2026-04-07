package com.martin.mvvm.ui.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.martin.mvvm.R;
import com.martin.mvvm.db.bean.User;
import com.martin.mvvm.ui.adpter.UserAdapter;
import com.martin.mvvm.ui.dialog.EditUserDialog;
import com.martin.mvvm.viewmodels.UserViewModel;

public class MainActivity extends AppCompatActivity {
    private UserViewModel userViewModel;
    private EditText nameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.user_rv);
        nameEditText = findViewById(R.id.et_username);
        passwordEditText = findViewById(R.id.et_password);
        UserAdapter adapter = new UserAdapter();
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 初始化 ViewModel
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        adapter.setItemDeleteListener(userViewModel::delete);
        adapter.setModifyListener(user -> {
            EditUserDialog dialog = new EditUserDialog(this, user, userViewModel::update);
            dialog.show();
        });
        // 观察 LiveData
        userViewModel.getUsers().observe(this, adapter::setUsers);

        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            if (!name.isEmpty() && !password.isEmpty()) {
                User user = new User();
                user.setName(name);
                user.setPassword(password);
                userViewModel.insert(user);
                nameEditText.setText("");
                passwordEditText.setText("");
            }
        });
    }
}
