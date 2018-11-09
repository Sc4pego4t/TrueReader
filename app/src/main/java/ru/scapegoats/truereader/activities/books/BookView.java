package ru.scapegoats.truereader.activities.books;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.Viewable;

public class BookView implements Viewable {

    BookActivity activity;
    public ViewPager vPagerReader;
    public RelativeLayout layout;

    BookView(BookActivity activity, View rootView) {
        this.activity = activity;
        vPagerReader=rootView.findViewById(R.id.vPagerReader);
        layout=rootView.findViewById(R.id.layout);
//        tvBookText.setLineSpacing(0,0.75f);
        //tvBookText.setText("");
    }

    //PDFView pdfView;

}
