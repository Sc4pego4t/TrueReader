package ru.scapegoats.truereader.activities.books;

import android.content.res.Configuration;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.Viewable;
import ru.scapegoats.truereader.utils.Utils;

public class BookView implements Viewable {

    public ViewPager vPagerReader;
    public RelativeLayout layout;
    BookActivity activity;
    private LinearLayout seekBarBackground;
    public SeekBar seekBar;
    public TextView pagesInfo;

    BookView(BookActivity activity, View rootView) {
        this.activity = activity;
        vPagerReader = rootView.findViewById(R.id.vPagerReader);
        layout = rootView.findViewById(R.id.layout);
        seekBar=rootView.findViewById(R.id.seekBar);
        pagesInfo=rootView.findViewById(R.id.pagesInfo);
        seekBarBackground = rootView.findViewById(R.id.seekBarBackground);

        if(activity.getResources().getConfiguration()
                .orientation== Configuration.ORIENTATION_PORTRAIT) {
            //change seek bar bottom margin to display it above navigation bar if orientation
            //is portrait

            RelativeLayout.LayoutParams params =
                    (RelativeLayout.LayoutParams) seekBarBackground.getLayoutParams();
            params.bottomMargin = Utils.getNavigationBarSize(activity);
            seekBarBackground.setLayoutParams(params);
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String info = i+1+"/"+seekBar.getMax();
                pagesInfo.setText(info);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                vPagerReader.setCurrentItem(seekBar.getProgress());
            }
        });

        vPagerReader.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("page",position+"");
                seekBar.setProgress(position);
                String info = position+1+"/"+seekBar.getMax();
                pagesInfo.setText(info);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void seekBarToggleVisibility(){
        if(seekBarBackground.getVisibility()==View.VISIBLE){
            seekBarBackground.setVisibility(View.INVISIBLE);
        } else {
            seekBarBackground.setVisibility(View.VISIBLE);
        }
    }

    //PDFView pdfView;

}
