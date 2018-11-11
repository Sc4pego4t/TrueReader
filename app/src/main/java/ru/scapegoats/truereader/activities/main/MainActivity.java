package ru.scapegoats.truereader.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.filebrowser.FileBrowserActivity;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;
import ru.scapegoats.truereader.modules.Viewable;
import ru.scapegoats.truereader.utils.ProgressDrawable;

public class MainActivity extends BaseActivity {
    //TODO andrey:fb2
    //TODO andrey:epub
    //TODO vlad:pdf
    //TODO vlad:djvu



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        String s1=new String("1");
        String s2=new String("1");

        Log.e("res",(s1==s2)+"");

        ProgressBar bar = findViewById(R.id.pb);
        bar.setIndeterminateDrawable(new ProgressDrawable(getResources().getIntArray(R.array.progress_colors)));
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
