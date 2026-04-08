package com.martin.mvvm.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel>
        extends AppCompatActivity {
    /**
     * TAG
     */
    protected final String TAG = this.getClass().getSimpleName();

    /**
     * ViewDataBinding
     */
    protected V binding;

    /**
     * ViewModel
     */
    protected VM mViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutId());
        handlerVM();
    }

    @SuppressWarnings("unchecked")
    private void handlerVM() {
        Class<BaseViewModel> viewModelClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            // 获取第二个泛型参数
            viewModelClass = (Class<BaseViewModel>)
                    ((ParameterizedType) type).getActualTypeArguments()[1];
        } else {
            viewModelClass = BaseViewModel.class;
        }
        mViewModel = (VM) new ViewModelProvider(this).get(viewModelClass);

        if (getVariableId() > 0) {
            // 自动绑定ViewModel到布局
            binding.setVariable(getVariableId(), mViewModel);
            binding.setLifecycleOwner(this);
            // 让ViewModel感知生命周期
            getLifecycle().addObserver(mViewModel);
        }
    }

    /**
     * Get LayoutId
     *
     * @return layout file ID
     */
    public abstract int getLayoutId();

    /**
     * Initialize the id of the ViewModel
     *
     * @return return BR's id
     */
    public abstract int getVariableId();

    /**
     * Destroy
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (binding != null) {
            binding.unbind();
        }
        binding = null;
    }
}
