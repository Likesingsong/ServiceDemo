package com.martin.mvvm.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.martin.mvvm.BR;
import com.martin.mvvm.R;
import com.martin.mvvm.base.BaseActivity;
import com.martin.mvvm.databinding.ActivityMainBinding;
import com.martin.mvvm.ui.adpter.UserAdapter;
import com.martin.mvvm.ui.dialog.EditUserDialog;
import com.martin.mvvm.viewmodels.UserViewModel;

public class MainActivity extends BaseActivity<ActivityMainBinding, UserViewModel> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserAdapter adapter = new UserAdapter();
        adapter.setItemDeleteListener(mViewModel::delete);
        adapter.setModifyListener(user -> {
            EditUserDialog dialog = new EditUserDialog(this, user, mViewModel::update);
            dialog.show();
        });
        binding.userRv.setAdapter(adapter);
        binding.userRv.setLayoutManager(new LinearLayoutManager(this));
        // 观察 LiveData
        mViewModel.getUsers().observe(this, adapter::setUsers);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public int getVariableId() {
        return BR.viewModel;
    }
}
