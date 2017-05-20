package com.duy.databaseservice.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.duy.databaseservice.R;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    final int mCount = 1;
    private final FragmentManager fm;
    private final Context context;


    public SectionsPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.fm = fm;
        this.context = context;
    }

    private String getString(int id) {
        return context.getString(id);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new FragmentEnterPassDoor();
            case 0:
                return new FragmentCommand();
            case 2:
                return new FragmentInfo();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 1:
                return getString(R.string.door_pass);
            case 0:
                return getString(R.string.cmd);
            case 2:
                return getString(R.string.info);
        }
        return null;
    }
}
