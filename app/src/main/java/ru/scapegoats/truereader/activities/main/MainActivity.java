package ru.scapegoats.truereader.activities.main;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.util.ArrayUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.filebrowser.FileBrowserActivity;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;
import ru.scapegoats.truereader.modules.Viewable;

public class MainActivity extends BaseActivity {
    //TODO andrey:fb2
    //TODO andrey:epub
    //TODO vlad:pdf
    //TODO vlad:djvu

    TextView tv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        tv=findViewById(R.id.tv);
        Display display=getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        Log.e("kk",(size.y-getStatusBarHeight()-getABSize())/tv.getLineHeight()+"");
        //IMPORTANT TO SET SPACING
        Log.e("kk",tv.getLineCount()+"");

    }


    @Override
    protected void onResume() {


        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Display display=getWindowManager().getDefaultDisplay();
        Point size=new Point();
        display.getSize(size);
        final View top = findViewById(R.id.top);
        final View bottom = findViewById(R.id.bottom);

        int topLoc[] = new int[2];
        top.getLocationOnScreen(topLoc);
        int BottomLoc[] = new int[2];
        bottom.getLocationOnScreen(BottomLoc);


        Log.e("dd", "topY: "+ topLoc[1]+" BottomY:" + BottomLoc[1]);
        Log.e("kk",getABSize()+"");
        Log.e("kk",(size.y-getStatusBarHeight()-getABSize())/tv.getLineHeight()+"");
        Log.e("kk",(float)(BottomLoc[1]-topLoc[1])/tv.getLineHeight()+"");
        Log.e("kk",size.y/tv.getTextSize()+"");

    }

    float getABSize(){
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        } else return 0;
    }
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }


        return result;
    }

    int getNavigationBarSize(){
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }


    @Override
    protected Viewable initView() {

        return new MainView(this, findViewById(android.R.id.content));
    }


    @Override
    protected Presenter initPresenter() {
        return new MainPresenter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_bar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fileBrowser:
                startActivity(
                        new Intent(this, FileBrowserActivity.class));

        }
        return true;
    }
}
