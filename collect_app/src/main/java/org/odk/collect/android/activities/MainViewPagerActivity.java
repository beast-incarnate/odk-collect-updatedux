package org.odk.collect.android.activities;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import org.odk.collect.android.R;
import org.odk.collect.android.adapters.ViewPagerAdapter;

public class MainViewPagerActivity extends AppCompatActivity {

    public static final String TAG = "MainViewPagerActivity";

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);
        setTheme(R.style.AppTheme);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        viewPager = (ViewPager)findViewById(R.id.view_pager);

        FragmentManager manager = getSupportFragmentManager();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(manager);

        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter.notifyDataSetChanged();

    }


}
