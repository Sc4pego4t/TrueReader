package ru.scapegoats.truereader.activities.books;

import android.content.Context;


import ru.scapegoats.truereader.modules.Viewable;

public class BookView implements Viewable {

    BookActivity activity;

    BookView(Context context){
        activity = (BookActivity)context;
    }

    //PDFView pdfView;

}
