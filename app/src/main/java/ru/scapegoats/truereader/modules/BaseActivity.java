package ru.scapegoats.truereader.modules;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.filebrowser.FileBrowserActivity;

public abstract class BaseActivity<T extends Viewable>
        extends AppCompatActivity {

    protected T view;
    protected Presenter<T> presenter;

    protected abstract T initView();
    protected abstract Presenter<T> initPresenter();

    @Override
    protected void onStart() {
        super.onStart();
        view=initView();
        presenter=initPresenter();
        presenter.onAttach(view);

    }


}
