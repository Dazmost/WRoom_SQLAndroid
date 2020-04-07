package com.example.waitingroomlist;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PatientFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public PatientFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PatientFragment();
        } else{
            return new StatisticsFragment();
        }
    }

    @Override
    public CharSequence getPageTitle (int position) {
        // Generate title based on item position
        if (position == 0) {
            return mContext.getString(R.string.category_patientList);
        } else {
            return mContext.getString(R.string.category_statistics);
        }
    }


    @Override
    public int getCount() {
        return 2;
    }
}
