package ru.scapegoats.truereader.activities.books.booktypes;

import ru.scapegoats.truereader.activities.books.booktypes.tools.PageDivider;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.BaseActivity;

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
        new PageDivider(activity, textSize, getBookTextContent()).createAdapter();
    }
    abstract String getBookTextContent();


}
