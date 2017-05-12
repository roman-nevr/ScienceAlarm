package org.berendeev.roma.sciencealarm.presentation.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.berendeev.roma.sciencealarm.presentation.presenter.BasePresenter;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {

    @Override protected void onStop() {
        super.onStop();
        if (!isChangingConfigurations()){

        }
    }

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initDi();
        initUi();
    }

    abstract protected BasePresenter getPresenter();

    abstract protected void initDi();

    abstract protected void initUi();

    abstract protected @LayoutRes int getLayoutId();
}
