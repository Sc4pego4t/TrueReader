package ru.scapegoats.truereader.activities.books.viewpager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import ru.scapegoats.truereader.R;

public class PageFragment extends Fragment{

    private SpannableString text;
    private static String TEXT="text";

    static PageFragment newInstance(SpannableString text){
        PageFragment fragment = new PageFragment();
        Bundle args=new Bundle();
        args.putCharSequence(TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text = (SpannableString) ((getArguments() != null) ? getArguments().get(TEXT) : "");
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.page_fragment, container, false);
        TextView pageText = result.findViewById(R.id.pageText);
        pageText.setText(text);

        //Pass our touch to parent activity to handle it
        pageText.setOnTouchListener((view, motionEvent) -> {
            Objects.requireNonNull(getActivity()).onTouchEvent(motionEvent);
            return true;
        });
        return result;
    }

}
