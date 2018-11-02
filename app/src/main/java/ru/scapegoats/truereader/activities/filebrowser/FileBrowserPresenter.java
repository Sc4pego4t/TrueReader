package ru.scapegoats.truereader.activities.filebrowser;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.scapegoats.truereader.activities.filebrowser.adapter.FileBrowserRVAdapter;
import ru.scapegoats.truereader.modules.Presenter;

public class FileBrowserPresenter implements Presenter<FileBrowserView> {
    String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onAttach(FileBrowserView view) {


        //check is permission granted
        if (ContextCompat.checkSelfPermission(view.activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            readFilesToRecycler(view);
        }
        //trying to acquire a permission
        else {
            ActivityCompat.requestPermissions(view.activity, permissions, 0);
        }
    }

    FileBrowserRVAdapter adapter;

    void readFilesToRecycler(FileBrowserView view) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

        adapter = new FileBrowserRVAdapter(
                file,
                view.llPathHistory,
                view.activity);


        view.rvFileList.setLayoutManager(new LinearLayoutManager(view.activity));
        view.rvFileList.setAdapter(adapter);
        view.rvFileList.setItemAnimator(new DefaultItemAnimator());
        view.rvFileList.addItemDecoration(
                new DividerItemDecoration(view.rvFileList.getContext(),
                        DividerItemDecoration.VERTICAL));
    }


    @Override
    public void onDetach() {


    }
}
