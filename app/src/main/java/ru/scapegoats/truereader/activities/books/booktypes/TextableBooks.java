package ru.scapegoats.truereader.activities.books.booktypes;

import android.util.Log;
import android.widget.TextView;


import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.booktypes.tools.PageDivider;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;

public abstract class TextableBooks {

    private BaseActivity activity;
    private float textSize;
    Book book;
    private TextableBooks(){}

    TextableBooks(BaseActivity activity, float textSize, Book book){
        this.activity=activity;
        this.textSize=textSize;
        this.book=book;



    }

    public void createAdapter() {
        ProgressDialog progressDialog=new ProgressDialog(activity
                ,activity.getString(R.string.progressDialogOpenFile));
        progressDialog.show();

        new PageDivider(activity, textSize, getBookTextContent(),progressDialog).createAdapter();
    }

    abstract String getBookTextContent();


}
