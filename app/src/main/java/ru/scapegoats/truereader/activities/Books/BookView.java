package ru.scapegoats.truereader.activities.Books;

import android.content.Context;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.Viewable;

public class BookView implements Viewable {

    BookActivity activity;

    BookView(Context context){
        activity = (BookActivity)context;
    }

    PDFView pdfView;
    public void loadPdfOnScreen(File file){
        activity.setContentView(R.layout.pdf_layout);
        pdfView = activity.findViewById(R.id.pdfView);
        pdfView.fromFile(file).enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                //.onDraw(onDrawListener)
                //.onDrawAll(onDrawListener)
                //.onLoad(onLoadCompleteListener)
                //.onPageChange(onPageChangeListener)
                //.onPageScroll(onPageScrollListener)
                //.onError(onErrorListener)
                //.onPageError(onPageErrorListener)
                //.onRender(onRenderListener)
                //.onTap(onTapListener)
                //.onLongPress(onLongPressListener)
                //.enableAnnotationRendering(false)
                //.password(null)
                .scrollHandle(null)
                .enableAntialiasing(true)
                .spacing(5)
                .load();
    }
}
