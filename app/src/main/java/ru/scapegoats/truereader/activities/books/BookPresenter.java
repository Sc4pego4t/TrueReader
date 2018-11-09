package ru.scapegoats.truereader.activities.books;

import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import io.reactivex.disposables.Disposable;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.booktypes.FB2;
import ru.scapegoats.truereader.activities.books.viewpager.MyPagerAdapter;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.Presenter;

public class BookPresenter implements Presenter<BookView> {

    private BookView view;
    public static String BOOK_INFO = "book_info";

    @Override
    public void onAttach(BookView view) {
        this.view = view;
        Book bookInfo = (Book) view.activity.getIntent().getExtras().get(BOOK_INFO);
        openBook(bookInfo);
    }

    private void openBook(Book book) {
        switch (book.getFileType()) {
            case PDF:
                openPDF(book);
                break;
            case DJVU:
                openDJVU(book);
                break;
            case EPUB:
                break;
            case FB2:
                openFB2(book);
                break;
            default:
                break;
        }
    }

    private void openPDF(Book book) {
        //view.loadPdfOnScreen(book.getFile());

    }

    private void openDJVU(Book book) {
    }

    private void openFB2(Book book) {
        float textSize=view.activity.getResources().getDimension(R.dimen.pageTextSize);
        new FB2(view.activity,textSize,book).createAdapter();



        //view.tvBookText.setText(pageList.get(0));
        //Log.e("GG", "OPEN Pdf");
    }




    @Override
    public void onDetach() {
        view = null;
    }
}
