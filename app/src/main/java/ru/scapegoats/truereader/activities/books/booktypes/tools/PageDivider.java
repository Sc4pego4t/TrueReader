package ru.scapegoats.truereader.activities.books.booktypes.tools;

import android.content.res.Resources;
import android.graphics.Point;
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


    volatile private Integer symbolsInRowCount=0, rowsCount;
    private float rowsMultiplyer=0.75f;
    BaseActivity activity;
    String text;

    public PageDivider(BaseActivity activity, float textSize, String text){
        this.activity=activity;
        this.text=text;
        getSymbolsInRowCount(textSize,activity);
        rowsCount=getRowsCount(true,true,true);

    }

    boolean isCreated=false;

    private void createAdapter(List<String> list){
        ((BookView)activity.view).vPagerReader.setAdapter(new MyPagerAdapter(activity,list));
    }

    Thread layout;
    private void getSymbolsInRowCount(float textSize, BaseActivity activity){
        View view=LayoutInflater.from(activity).inflate(R.layout.page_fragment,null);
        ((BookView)activity.view).layout.addView(view);
        view.setVisibility(View.INVISIBLE);
        //size = ((TextView) view).getLayout().getLineEnd(0);

         view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
             if(!isCreated) {
                 isCreated=true;
                 Log.e("why", ((TextView) view).getLayout().getLineEnd(0) + 1 + "");
                 symbolsInRowCount = ((TextView) view).getLayout().getLineEnd(0) + 1;
                 createAdapter(getPages(text));
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

    static private String newLine="<br>";


    private List<String> getPages(String text) {
        List<String> pageList = new ArrayList<>();
        String divider = " ";

        Log.e("rows and cols", rowsCount + "//" + symbolsInRowCount);
        int currentRow = 0, currentColumn = 0;

        StringBuilder builder = new StringBuilder();

        StringTokenizer tokenizer = new StringTokenizer(text, divider);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            char[] array = token.toCharArray();
            builder.append(array);
//            Log.e("token",token+pageList.size());

            if (array[0] == '\n' || token.equals(newLine)) {
                currentRow++;
                currentColumn = 0;
                if (currentRow >= rowsCount-1) {
                    pageList.add(builder.toString());
                    builder = new StringBuilder();
                    currentRow = 0;
                }
            } else {
                builder.append(divider);
            }


            int size = array.length;
            currentColumn += size+1;
            if (currentColumn >= symbolsInRowCount-1) {
                currentColumn = size;
                currentRow++;
                //Log.e("ROWS",currentRow+" "+currentColumn);
                if (currentRow >= rowsCount) {
                    pageList.add(builder.toString());
                    builder = new StringBuilder();
                    currentRow = 0;
                }
            }
        }
        pageList.add(builder.toString());
        //pageList.add(currentPageText.toString());
        return pageList;
    }

}
