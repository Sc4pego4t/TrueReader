package ru.scapegoats.truereader.activities.Books;

import ru.scapegoats.truereader.model.Book;
import ru.scapegoats.truereader.modules.Presenter;

public class BookPresenter implements Presenter<BookView> {

    private BookView view;

    BookPresenter(Book book){
        switch (book.getFileType()){
            case DPF:
                openPDF(book);
                break;
            case DJVU:
                openDJVU(book);
                break;
            case EPUB:
                break;
            case FB2:
                break;
            default:
                break;
        }
    }

    private void openPDF(Book book){
        view.loadPdfOnScreen(book.getFile());
    }

    private void openDJVU(Book book){

    }

    @Override
    public void onAttach(BookView view) {
        this.view = view;
    }

    @Override
    public void onDetach() {
        view = null;
    }
}
