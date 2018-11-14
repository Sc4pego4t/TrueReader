package ru.scapegoats.truereader.activities.books;

import ru.scapegoats.truereader.activities.books.booktypes.EPUB;
import ru.scapegoats.truereader.activities.books.booktypes.FB2;
import ru.scapegoats.truereader.activities.books.booktypes.HTML;
import ru.scapegoats.truereader.activities.books.booktypes.TXT;
import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.Presenter;
import ru.scapegoats.truereader.modules.ProgressDialog;

public class BookPresenter implements Presenter<BookView> {

    private BookView view;
    public static String BOOK_INFO = "book_info";

    @Override
    public void onAttach(BookView view) {
        this.view = view;
        Book bookInfo = (Book) view.activity.getIntent().getExtras().get(BOOK_INFO);
        openBook(bookInfo);
    }

    ProgressDialog progressDialog;
    private void openBook(Book book) {
        progressDialog=new ProgressDialog(view.activity);
        progressDialog.show();
        switch (book.getFileType()) {
            case PDF:
                openPDF(book);
                break;
            case DJVU:
                openDJVU(book);
                break;
            case EPUB:
                openEPUB(book);
                break;
            case FB2:
                openFB2(book);
                break;
            case TXT:
                openTXT(book);
            case HTML:
                openHTML(book);
            default:
                break;
        }
    }

    private void openHTML(Book book) {
        new HTML(view.activity,book).createAdapter(progressDialog);
    }

    private void openTXT(Book book) {
        new TXT(view.activity,book).createAdapter(progressDialog);
    }

    private void openEPUB(Book book) {
        new EPUB(view.activity,book).createAdapter(progressDialog);
    }

    private void openPDF(Book book) {
        //view.loadPdfOnScreen(book.getFile());

    }

    private void openDJVU(Book book) {
    }

    private void openFB2(Book book) {
        new FB2(view.activity,book).createAdapter(progressDialog);
    }




    @Override
    public void onDetach() {
        view = null;
    }
}
