package com.example.abhishekrawat.questionstudy.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhishekrawat.questionstudy.Adapter.MainPagerAdapter;
import com.example.abhishekrawat.questionstudy.R;
import com.example.abhishekrawat.questionstudy.Util.DataUtil;
import com.example.abhishekrawat.questionstudy.Util.FragmentUtil;

public class MainFragment extends Fragment {

    private MainPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    MainFragmentListener mPageListener;

    // TODO: Rename and change types and number of parameters
    public static MainFragment getInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSectionsPagerAdapter = new MainPagerAdapter(getChildFragmentManager(), DataUtil.tabs.length);
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        for (String tab : DataUtil.tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab).setCustomView(getTabView(DataUtil.getTab(tab))));
        }
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }
    public View getTabView(int position) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.lyt_tab, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        switch (position) {
            case DataUtil.TabsTag.HOME:
                icon.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_home));
                break;
            case DataUtil.TabsTag.GROUP:
                icon.setImageDrawable(this.getContext().getResources().getDrawable(R.drawable.ic_group));
                break;
            default:
        }
        return view;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPageListener = (MainFragmentListener) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

    public interface MainFragmentListener extends ActivityListener {
    }
}
