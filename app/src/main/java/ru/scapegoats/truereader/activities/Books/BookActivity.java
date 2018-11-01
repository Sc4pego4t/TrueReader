package ru.scapegoats.truereader.activities.Books;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.github.barteksc.pdfviewer.PDFView;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;

public class BookActivity extends BaseActivity<BookView> {

    Book currentBook;

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
        return new BookPresenter(currentBook);
    }
}
