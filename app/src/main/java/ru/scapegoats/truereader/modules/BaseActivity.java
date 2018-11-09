package ru.scapegoats.truereader.modules;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity<T extends Viewable>
        extends AppCompatActivity {

    public T view;
    protected Presenter<T> presenter;

    protected abstract T initView();

    protected abstract Presenter<T> initPresenter();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = initView();
        presenter = initPresenter();
        presenter.onAttach(view);
    }

}
