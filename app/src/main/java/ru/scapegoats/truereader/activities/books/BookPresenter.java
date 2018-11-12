package ru.scapegoats.truereader.activities.books;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.booktypes.FB2;
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
        new FB2(view.activity,book).createAdapter();
    }




    @Override
    public void onDetach() {
        view = null;
    }
}
