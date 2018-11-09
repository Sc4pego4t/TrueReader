package ru.scapegoats.truereader.activities.books.viewpager;

import android.view.View;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import ru.scapegoats.truereader.modules.BaseActivity;

public class MyPagerAdapter extends FragmentStatePagerAdapter {

    private List<String> pages;
    private BaseActivity activity;
    public MyPagerAdapter(BaseActivity activity, List<String> pages){
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
