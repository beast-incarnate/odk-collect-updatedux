package org.odk.collect.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import org.odk.collect.android.fragments.AllFragment;
import org.odk.collect.android.fragments.FinalisedFragment;
import org.odk.collect.android.fragments.SentFragment;

/**
 * Created by kunalsingh on 13/03/17.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public static final String TABS[] = {"All" , "Finalised" , "Sent"};
    public static final String TAG = "VPA";

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){

            case 0 :
                return new AllFragment();
            case 1 :
                return  new FinalisedFragment();
            case 2 :
                Log.d(TAG,"calling sent");
                return new SentFragment();
            default:
                Log.d(TAG,"default calle");
                return null;

        }

    }

    @Override
    public int getCount() {
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return TABS[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
