package ru.scapegoats.truereader.activities.books.booktypes;

import android.text.SpannableString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.scapegoats.truereader.activities.books.booktypes.tools.PageDivider;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;

public class TXT implements TextableFormats {
    private BaseActivity activity;
    private Book book;

    public TXT(BaseActivity activity, Book book) {
        this.activity = activity;
        this.book = book;
    }

    @Override
    public void createAdapter() {
        ProgressDialog progressDialog=new ProgressDialog(activity);
        progressDialog.show();
        Disposable disposable= Single.fromCallable(()->{
            StringBuilder builder = new StringBuilder("\n\t\t\t\t");
            try {
                FileInputStream reader=new FileInputStream(book.getFile());

                Scanner scanner=new Scanner(reader,"windows-1251");

                while (scanner.hasNextLine()) {
                    builder.append(scanner.nextLine()).append("\n\t\t\t\t");
                }

                reader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((str)->{

                    new PageDivider(activity
                            ,new SpannableString(str.toString()),progressDialog).createAdapter();
                });

    }
}
