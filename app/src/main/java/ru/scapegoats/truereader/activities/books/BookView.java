package ru.scapegoats.truereader.activities.books;

import android.app.ActionBar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.viewpager.widget.ViewPager;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.Viewable;
import ru.scapegoats.truereader.utils.Utils;

public class BookView implements Viewable {

    public ViewPager vPagerReader;
    public RelativeLayout layout;
    BookActivity activity;
    LinearLayout seekBar;

    BookView(BookActivity activity, View rootView) {
        this.activity = activity;
        vPagerReader = rootView.findViewById(R.id.vPagerReader);
        layout = rootView.findViewById(R.id.layout);


        //change seek bar bottom margin to display it above navigation bar
        seekBar = rootView.findViewById(R.id.seekBarBackground);
        RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)seekBar.getLayoutParams();
        params.bottomMargin = Utils.getNavigationBarSize(activity)+(int) activity.getResources().getDimension(R.dimen.seekBarMargin);
        seekBar.setLayoutParams(params);
    }

    public void seekBarToggleVisibility(){
        if(seekBar.getVisibility()==View.VISIBLE){
            seekBar.setVisibility(View.INVISIBLE);
        } else {
            seekBar.setVisibility(View.VISIBLE);
        }
    }

    //PDFView pdfView;

}
