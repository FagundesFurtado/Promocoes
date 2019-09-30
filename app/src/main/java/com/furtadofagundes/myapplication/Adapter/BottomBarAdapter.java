package com.furtadofagundes.myapplication.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BottomBarAdapter extends SmartFragmentStatePagerAdapter {
    private final List<Fragment> fragments = new ArrayList<>();


    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        return super.instantiateItem(container, position);
    }

    public BottomBarAdapter(FragmentManager fragmentManager ) {

        super(fragmentManager);

    }

    public void addFragments(Fragment fragment) {
        fragments.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }




    @Override
    public int getCount() {
        return fragments.size();
    }
}