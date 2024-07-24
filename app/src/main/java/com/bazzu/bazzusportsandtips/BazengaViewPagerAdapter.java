package com.bazzu.bazzusportsandtips;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class BazengaViewPagerAdapter extends FragmentStateAdapter {


    public BazengaViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Football();
            case 1:
                return new OtherSports();
            case 2:
                return new JackPotsBazenga();
            default:
                return new Football();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

}
