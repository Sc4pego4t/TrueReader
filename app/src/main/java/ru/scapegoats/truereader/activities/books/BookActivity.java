package ru.scapegoats.truereader.activities.books;

import android.os.Bundle;


import androidx.annotation.Nullable;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;

public class BookActivity extends BaseActivity<BookView> {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    protected BookView initView() {
        return new BookView(this);
    }

    @Override
    protected Presenter<BookView> initPresenter() {
        return new BookPresenter();
    }
}
