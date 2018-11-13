package ru.scapegoats.truereader.activities.books.booktypes;

import android.text.SpannableString;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

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
        book.getFile();

        StringBuilder builder = new StringBuilder();
        try {
            FileReader reader=new FileReader(book.getFile());
            Scanner scanner=new Scanner(reader);

            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }

            reader.close();
            ProgressDialog progressDialog=new ProgressDialog(activity);
            new PageDivider(activity
                    ,new SpannableString(builder.toString()),progressDialog).createAdapter();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
