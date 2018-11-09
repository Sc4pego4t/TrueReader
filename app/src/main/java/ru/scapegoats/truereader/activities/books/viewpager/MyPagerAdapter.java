package ru.scapegoats.truereader.activities.books.viewpager;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
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
