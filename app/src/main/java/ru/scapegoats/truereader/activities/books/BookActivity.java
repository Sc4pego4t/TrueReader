package ru.scapegoats.truereader.activities.books;

import android.os.Bundle;

import androidx.annotation.Nullable;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;

public class BookActivity extends BaseActivity<BookView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.read_layout);
        super.onCreate(savedInstanceState);
    }


    @Override
    protected BookView initView() {
        return new BookView(this,findViewById(android.R.id.content));
    }

    @Override
    protected Presenter<BookView> initPresenter() {
        return new BookPresenter();
    }
}
