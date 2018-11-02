package ru.scapegoats.truereader.activities.filebrowser;

import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.BaseActivity;
import ru.scapegoats.truereader.modules.Viewable;


public class FileBrowserView implements Viewable {

    RecyclerView rvFileList;
    LinearLayout llPathHistory;
    BaseActivity activity;
    HorizontalScrollView scrollView;

    FileBrowserView(BaseActivity context, View view) {
        llPathHistory = view.findViewById(R.id.pathHistory);
        rvFileList = view.findViewById(R.id.filesList);
        this.activity = context;
        scrollView = view.findViewById(R.id.scroll);


        llPathHistory.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View view, View view1) {
                scrollView.post(() -> {
                    scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
                    rvFileList.scrollToPosition(0);
                });

            }

            @Override
            public void onChildViewRemoved(View view, View view1) {

            }
        });
    }
}
