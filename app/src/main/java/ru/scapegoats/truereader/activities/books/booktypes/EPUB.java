package ru.scapegoats.truereader.activities.books.booktypes;

import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;

public class EPUB implements TextableFormats {

    private BaseActivity activity;
    private Book book;
    //TODO RESTORE text after orientation changes

    public EPUB(BaseActivity activity, Book book) {
        this.activity = activity;
        this.book = book;
    }
    @Override
    public void createAdapter(ProgressDialog progressDialog) {

    }
}
