package com.commerzi.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.commerzi.app.customers.CustomerFragment;
import com.commerzi.app.profile.ProfileFragment;
import com.commerzi.app.route.actualRoute.ActualRouteFragment;
import com.commerzi.app.route.plannedRoute.PlannedRouteFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    private static final int NB_FRAGMENT = 4;
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
                return PlannedRouteFragment.newInstance();
            case 1 :
                return ActualRouteFragment.newInstance();
            case 2 :
                return CustomerFragment.newInstance();
            case 3 :
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