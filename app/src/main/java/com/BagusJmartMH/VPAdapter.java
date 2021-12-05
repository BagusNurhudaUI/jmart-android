package com.BagusJmartMH;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class VPAdapter extends FragmentPagerAdapter {
    public VPAdapter(@NonNull FragmentManager FragManager){
        super(FragManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ProductsFragment.newInstance();
            case 1:
                return FilterFragment.newInstance();
            default:
                return ProductsFragment.newInstance();
        }
    }


    @Override
    public int getCount() {
        return 2;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle (int position){
        switch (position){
            case 0:
                return "Products";
            case 1:
                return "Filter";
            default:
                return "Default";
        }
    }
}
