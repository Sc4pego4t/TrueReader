package ru.scapegoats.truereader.activities.books.booktypes.tools;

import android.content.res.Resources;
import android.graphics.Point;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.operators.single.SingleUsing;
import io.reactivex.observers.DisposableObserver;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.BookView;
import ru.scapegoats.truereader.activities.books.booktypes.TextableBooks;
import ru.scapegoats.truereader.activities.books.viewpager.MyPagerAdapter;
import ru.scapegoats.truereader.modules.BaseActivity;

public class PageDivider {


    private int rowsCount;
    private float rowsMultiplyer=0.75f;
    private BaseActivity activity;
    private String text;

    public PageDivider(BaseActivity activity, float textSize, String text){
        this.activity=activity;
        this.text=text;
        rowsCount=getRowsCount(true,true,true);
        divideOnPage(textSize,activity);


    }

    boolean isCreated=false;

    private void createAdapter(List<String> list){
        ((BookView)activity.view).vPagerReader.setAdapter(new MyPagerAdapter(activity,list));
    }


    Thread layout;
    private void  divideOnPage(float textSize, BaseActivity activity){
        List<String> pages = new ArrayList<>();
        View view=LayoutInflater.from(activity).inflate(R.layout.page_fragment,null);
        ((BookView)activity.view).layout.addView(view);
        ((TextView)view).setText(text);
        view.setVisibility(View.INVISIBLE);
        //size = ((TextView) view).getLayout().getLineEnd(0);
         view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
             if(!isCreated) {
                 isCreated=true;
                 int totalRowsCount = ((TextView) view).getLineCount();
                 Layout layout=((TextView) view).getLayout();
                 for (int i = 0; i < totalRowsCount-rowsCount; i+=rowsCount) {
                     int start=layout.getLineStart(i);
                     int end=layout.getLineEnd(i+rowsCount-1);
                     pages.add(((TextView) view).getText().subSequence(start,end).toString());
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
        float textViewHeight=getDisplayHeight() - getActionBarHeight()-getStatusBarHeight()-
                activity.getResources().getDimension(R.dimen.margin)*2;
        return (int)(textViewHeight/getRowHeight());
    }

    private float getRowHeight(){
        View view=LayoutInflater.from(activity).inflate(R.layout.page_fragment,null);
        return ((TextView)view).getLineHeight();
    }

    private float getActionBarHeight(){
        TypedValue tv = new TypedValue();
        if (activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,activity.getResources().getDisplayMetrics());
        } else return 0;
    }

    private float getStatusBarHeight() {
        float result = 0;
        int resourceId = activity.getResources().getIdentifier(
                "status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = activity.getResources().getDimensionPixelSize(resourceId);

        }
        return result;
    }

    private int getNavigationBarSize(){
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier(
                "navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }



}
