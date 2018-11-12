package ru.scapegoats.truereader.activities.books.booktypes.tools;

import android.content.res.Configuration;
import android.graphics.Point;
import android.text.Layout;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private BaseActivity activity;
    private SpannableString text;
    private ProgressDialog progressDialog;

    public PageDivider(BaseActivity activity, SpannableString text, ProgressDialog progressDialog){
        this.activity=activity;
        this.text=text;
        this.progressDialog=progressDialog;
        rowsCount=getRowsCount();
    }

    public void createAdapter(){
        divideOnPages(activity);
    }

    private boolean isCreated=false;

    private void createAdapter(List<SpannableString> list){
        ((BookView)activity.view).vPagerReader.setAdapter(new MyPagerAdapter(activity,list));
    }


    //TODO change of orientation takes a lot of time
    // , so maybe we should provide possibility of changing orientation only in settings of our app
    private void divideOnPages(BaseActivity activity) {

        List<SpannableString> pages = new ArrayList<>();
        View view = LayoutInflater.from(activity).inflate(R.layout.page_fragment, null);
        TextView textView = view.findViewById(R.id.pageText);

        view.setVisibility(View.INVISIBLE);

        ((BookView) activity.view).layout.addView(view);

        textView.setText(text);

        view.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (!isCreated) {
                Layout layout = textView.getLayout();
                isCreated = true;

                //TODO handle disposable
                Disposable disposable = Single.fromCallable(() -> {

                    //create callback what return inflated with text layout
                    return new StaticLayout(text
                            , layout.getPaint()
                            , layout.getWidth()
                            , layout.getAlignment()
                            , layout.getSpacingMultiplier()
                            , layout.getSpacingAdd()
                            , false);
                        }).subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((inflatedLayout) -> {

                            //divide text from inflated layout on pages
                            view.setVisibility(View.GONE);
                            for (int i = 0; i < inflatedLayout.getLineCount() - rowsCount; i += rowsCount) {
                                int start = inflatedLayout.getLineStart(i);
                                int end = inflatedLayout.getLineEnd(i + rowsCount - 1);
                                pages.add((SpannableString)inflatedLayout.getText().subSequence(start, end));
                            }
                            ((BookView) activity.view).seekBar.setMax(pages.size());
                            ((BookView) activity.view).pagesInfo.setText(1 + "/" + pages.size());
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

    private int getRowsCount() {
        float textViewHeight = getDisplayHeight()
                - activity.getResources().getDimension(R.dimen.margin)*2;

        //add navigation bar size if orientation is portrait
        if(activity.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            textViewHeight+=+ Utils.getNavigationBarSize(activity);
        }

        return (int)(textViewHeight/getRowHeight());
    }

    private float getRowHeight(){
        View view=LayoutInflater.from(activity).inflate(R.layout.page_fragment,null);
        TextView textView=view.findViewById(R.id.pageText);
        return textView.getLineHeight();
    }




}
