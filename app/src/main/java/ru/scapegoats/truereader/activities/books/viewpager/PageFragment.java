package ru.scapegoats.truereader.activities.books.viewpager;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ru.scapegoats.truereader.R;

public class PageFragment extends Fragment {

    private String text;
    private static String TEXT="text";

    static PageFragment newInstance(String text){
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putString(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = (getArguments() != null) ? getArguments().getString(TEXT) : "";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.page_fragment, container, false);
        TextView pageText = result.findViewById(R.id.pageText);
        //pageText.setLineSpacing(0,0.75f);
        Log.e("Lines",pageText.getLineCount()+"");
        pageText.setText(text);
        return result;
    }


}
