package ru.scapegoats.truereader.activities.books.viewpager;

import android.text.SpannableString;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.scapegoats.truereader.R;
import ru.scapegoats.truereader.modules.BaseActivity;

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    private List<SpannableString> pages;
    private BaseActivity activity;
    public MyPagerAdapter(BaseActivity activity, List<SpannableString> pages){
        super(activity.getSupportFragmentManager());
        this.pages=pages;
        this.activity=activity;
    }


    @Override
    public Fragment getItem(int position) {

        return PageFragment.newInstance(pages.get(position));

    }



    @Override
    public int getCount() {
        return pages.size();
    }
}
