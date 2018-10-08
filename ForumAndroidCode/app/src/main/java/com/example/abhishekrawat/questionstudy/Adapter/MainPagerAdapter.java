package com.example.abhishekrawat.questionstudy.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.DataUtil;
import com.example.abhishekrawat.questionstudy.ui.GroupFragment;
import com.example.abhishekrawat.questionstudy.ui.HomeFragment;


public class MainPagerAdapter extends FragmentPagerAdapter {

    int tabCount;
    public MainPagerAdapter(FragmentManager fm, int count) {
        super(fm);
        this.tabCount = count;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case DataUtil.TabsTag.HOME:
                return HomeFragment.newInstance();
            case DataUtil.TabsTag.GROUP:
                return GroupFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}

