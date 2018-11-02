package ru.scapegoats.truereader.activities.filebrowser;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.activities.filebrowser.adapter.FileBrowserRVAdapter;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Presenter;

public class FileBrowserActivity extends BaseActivity<FileBrowserView> {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.file_browser_layout);
        super.onCreate(savedInstanceState);
        Log.e("STEP", "create");
    }


    private static String ADAPTER_STATE = "adapter";

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ADAPTER_STATE, ((FileBrowserPresenter) presenter).adapter);
    }

    //restore progress after rotating or other actions
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        FileBrowserRVAdapter adapter = savedInstanceState.getParcelable(ADAPTER_STATE);

        adapter.setLlFilePathHistory(view.llPathHistory);
        adapter.setActivity(this);

        ((FileBrowserPresenter) presenter).adapter = adapter;
        view.rvFileList.setAdapter(adapter);
        adapter.restoreProgress();


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
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            ((FileBrowserPresenter) presenter).readFilesToRecycler(view);
        }

    }

    @Override
    public void onBackPressed() {
        FileBrowserRVAdapter adapterRef = ((FileBrowserPresenter) presenter).adapter;
        if (adapterRef.getHist().size() == 1) {
            super.onBackPressed();
        } else {
            adapterRef.histBack();
        }
    }


}
