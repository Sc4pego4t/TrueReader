package ru.scapegoats.truereader.activities.books.booktypes;

import android.text.SpannableString;
import android.util.Log;
import android.widget.Toast;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

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
    public void createAdapter(ProgressDialog progressDialog) {


        Disposable disposable = Single.fromCallable(() -> {


            StringBuilder builder = new StringBuilder("\n\t\t\t\t");
            try {

                //get file encoding with help of mozilla library
                FileInputStream fis = new FileInputStream(book.getFile());

                UniversalDetector detector = new UniversalDetector(null);

                byte[] buf = new byte[4096];
                int nread;
                while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                    detector.handleData(buf, 0, nread);
                }
                detector.dataEnd();

                String encoding = detector.getDetectedCharset();
                if (encoding != null) {
                    Log.e("Detected encoding = ", encoding);
                } else {
                    Log.e("No encoding detected.", "((");
                    return null;
                }

                detector.reset();


                FileInputStream reader = new FileInputStream(book.getFile());

                Scanner scanner = new Scanner(reader, encoding);

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
                .subscribe((str) -> {

                    if (str == null) {

                        //TODO replace toast with dialog
                        Toast.makeText(activity,
                                "Unable to open file, reason - file has strange file encoding"
                                , Toast.LENGTH_LONG).show();
                    } else {
                        new PageDivider(activity
                                , new SpannableString(str.toString()), progressDialog).createAdapter();
                    }
                });

    }
}
