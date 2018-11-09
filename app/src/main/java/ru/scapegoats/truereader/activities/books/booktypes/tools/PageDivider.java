package ru.scapegoats.truereader.activities.books.booktypes.tools;

import android.graphics.Point;
import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.BookView;
import ru.scapegoats.truereader.activities.books.viewpager.MyPagerAdapter;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.utils.Utils;

public class PageDivider {


    private int rowsCount;
    private float textSize;
    private BaseActivity activity;
    private String text;

    public PageDivider(BaseActivity activity, float textSize, String text){
        this.activity=activity;
        this.text=text;
        this.textSize=textSize;
        rowsCount=getRowsCount(true,true,true);

    }

    public void createAdapter(){
        divideOnPage(textSize,activity);
    }

    private boolean isCreated=false;

    private void createAdapter(List<String> list){
        ((BookView)activity.view).vPagerReader.setAdapter(new MyPagerAdapter(activity,list));
    }

    private void  divideOnPage(float textSize, BaseActivity activity){
        List<String> pages = new ArrayList<>();
        View view=LayoutInflater.from(activity).inflate(R.layout.page_fragment,null);
        TextView textView=view.findViewById(R.id.pageText);
        ((BookView)activity.view).layout.addView(view);

        textView.setText(text);
        view.setVisibility(View.INVISIBLE);

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
             if(!isCreated) {
                 isCreated=true;
                 int totalRowsCount = textView.getLineCount();
                 view.setVisibility(View.GONE);
                 Layout layout=textView.getLayout();
                 for (int i = 0; i < totalRowsCount-rowsCount; i+=rowsCount) {
                     int start=layout.getLineStart(i);
                     int end=layout.getLineEnd(i+rowsCount-1);
                     pages.add(textView.getText().subSequence(start,end).toString());
                 }
                 createAdapter(pages);
             }
        });
    }

    private float getDisplayHeight(){
        Display display=activity.getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        return size.y;
    }

    private int getRowsCount(boolean actionBarExist
            , boolean statusBarExist
            , boolean navigationBarExist) {
        //TODO: add possibility to calculate without ab,sb,nb
        float textViewHeight=getDisplayHeight() + Utils.getNavigationBarSize(activity) -
                activity.getResources().getDimension(R.dimen.margin)*2;
        return (int)(textViewHeight/getRowHeight());
    }

    private float getRowHeight(){
        View view=LayoutInflater.from(activity).inflate(R.layout.page_fragment,null);
        TextView textView=view.findViewById(R.id.pageText);
        return textView.getLineHeight();
    }





}
