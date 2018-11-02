package ru.scapegoats.truereader.activities.books;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
        Log.e("GG", "OPEN Pdf");
    }

    private void openDJVU(Book book) {
    }

    private void openFB2(Book book) {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            try {
                Document document = builder.parse(book.getFile());
                Element element = document.getDocumentElement();
                Log.e("el", element.getTagName());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            Log.e("exc", e.toString());
        }

    }


    @Override
    public void onDetach() {
        view = null;
    }
}
