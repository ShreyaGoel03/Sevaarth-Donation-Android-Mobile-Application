package com.example.sevaarth;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdaptor extends FragmentPagerAdapter {

    int tab_count;
    int type;

    public PageAdaptor(@NonNull FragmentManager fm, int behavior, int type) {
        super(fm, behavior);
        tab_count = behavior;
        this.type = type;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            Log.d("RUN","PA prev");
            return  new PreviousDonations(type);
        }
        else if(position == 1){
            Log.d("RUN", "PA curr");
            return  new CurrentDonations(type);
        }
        return null;
    }

    @Override
    public int getCount() {
        return tab_count;
    }
}
