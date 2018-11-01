package ru.scapegoats.truereader.activities.filebrowser;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.Nullable;

import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;
import ru.scapegoats.truereader.modules.Viewable;

public class FileBrowserActivity extends BaseActivity<FileBrowserView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_browser_layout);
    }

    @Override
    protected FileBrowserView initView() {
        return new FileBrowserView(this, findViewById(android.R.id.content));
    }

    @Override
    protected Presenter<FileBrowserView> initPresenter() {
        return new FileBrowserPresenter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            ((FileBrowserPresenter)presenter).readFilesToRecycler(view);
        }

    }

    @Override
    public void onBackPressed() {
        FileBrowserRVAdapter adapterRef = ((FileBrowserPresenter)presenter).adapter;
        if(adapterRef.getHist().size()==2) {
            super.onBackPressed();
        } else {
            adapterRef.histBack();
        }
    }
}
