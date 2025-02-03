package com.commerzi.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class FragmentAdapter extends FragmentStateAdapter {
    private static final int NB_FRAGMENT = 3;
    /**
     * Constructor
     * @param activity activity containing the ViewPager2 managing the fragments
     */
    public FragmentAdapter(FragmentActivity activity) {
        super(activity);
    }

    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0 :
                return RouteFragment.newInstance();
            case 1 :
                return ClientFragment.newInstance();
            case 2 :
                return ProfileFragment.newInstance();
            default :
                return null;
        }
    }
    @Override
    public int getItemCount() {
        return NB_FRAGMENT;
    }
}