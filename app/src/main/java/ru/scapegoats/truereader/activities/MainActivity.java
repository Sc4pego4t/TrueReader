package ru.scapegoats.truereader.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;
import ru.scapegoats.truereader.modules.Viewable;

public class MainActivity extends BaseActivity {
    //TODO andrey:fb2
    //TODO andrey:epub
    //TODO vlad:pdf
    //TODO vlad:djvu

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
    }

    @Override
    protected Viewable initView() {
        return new MainView(this,findViewById(android.R.id.content));
    }

    @Override
    protected Presenter initPresenter() {
        return new MainPresenter();
    }
}
