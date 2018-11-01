package ru.scapegoats.truereader.modules;

import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity<T extends Viewable>
        extends AppCompatActivity {

    T view;
    Presenter<T> presenter;

    protected abstract T initView();
    protected abstract Presenter<T> initPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        view = initView();
        presenter = initPresenter();
        presenter.onAttach(view);
    }
}
