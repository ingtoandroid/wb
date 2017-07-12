package com.example.a.app10.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.a.app10.Fragment.ExpertFragment;
import com.example.a.app10.Fragment.IndexFragment;
import com.example.a.app10.Fragment.InfoFragment;

/**
 * Created by 12917 on 2017/6/2.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final int COUNTS=3;
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment= new IndexFragment();
                break;
            case 1:
                fragment= new ExpertFragment();
                break;
            case 2:
                fragment= new InfoFragment();
                break;
            default:
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return COUNTS;
    }

}
