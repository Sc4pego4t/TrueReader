package ru.scapegoats.truereader.activities.books.booktypes.tools;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.books.BookView;
import ru.scapegoats.truereader.activities.books.viewpager.MyPagerAdapter;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.ProgressDialog;
import ru.scapegoats.truereader.utils.Utils;

public class PageDivider {


    private int rowsCount;
    private float textSize;
    private BaseActivity activity;
    private String text;
    private ProgressDialog progressDialog;

    public PageDivider(BaseActivity activity, float textSize, String text, ProgressDialog progressDialog){
        this.activity=activity;
        this.text=text;
        this.textSize=textSize;
        this.progressDialog=progressDialog;

        rowsCount=getRowsCount(true,true,true);

    }

    public void createAdapter(){
        divideOnPage(textSize,activity);
    }

    private boolean isCreated=false;

    private void createAdapter(List<String> list){
        ((BookView)activity.view).vPagerReader.setAdapter(new MyPagerAdapter(activity,list));
    }

    private void  divideOnPage(float textSize, BaseActivity activity) {

        List<String> pages = new ArrayList<>();
        View view = LayoutInflater.from(activity).inflate(R.layout.page_fragment, null);
        TextView textView = view.findViewById(R.id.pageText);

        view.setVisibility(View.INVISIBLE);

        ((BookView) activity.view).layout.addView(view);


        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!isCreated) {
                Layout layout = textView.getLayout();
                isCreated = true;

                Disposable disposable = Single.fromCallable(()->{
                        Log.e("thread",Thread.currentThread().getName());
                        //create callback what return inflated with text layout
                        return new StaticLayout(text
                        , layout.getPaint()
                        , layout.getWidth()
                        , layout.getAlignment()
                        , layout.getSpacingMultiplier()
                        , layout.getSpacingAdd()
                        , false); })
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((inflatedLayout)->{

                            //divide text from inflated layout on pages
                            Log.e("thread",Thread.currentThread().getName());
                            view.setVisibility(View.GONE);
                            for (int i = 0; i < inflatedLayout.getLineCount() - rowsCount; i += rowsCount) {
                                int start = inflatedLayout.getLineStart(i);
                                int end = inflatedLayout.getLineEnd(i + rowsCount - 1);
                                pages.add(inflatedLayout.getText().subSequence(start, end).toString());
                            }

                            progressDialog.cancel();
                            createAdapter(pages);
                        });
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
