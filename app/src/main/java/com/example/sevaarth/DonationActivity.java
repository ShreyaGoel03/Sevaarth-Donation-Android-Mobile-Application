package com.example.sevaarth;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class DonationActivity extends AppCompatActivity {
    TabLayout tabLayout;
    TabItem prev_item, curr_item;
    ViewPager vpg;
    PageAdaptor pageAdaptor;
    Spinner sort;
    private ArrayAdapter<String> sort_adaptor;
    private int type = 0;

    @Override
    protected void onCreate(Bundle savedInstancestate) {
        Log.d("RUN", "Create");
        super.onCreate(savedInstancestate);
        setContentView(R.layout.donation_activity);
        tabLayout = (TabLayout)findViewById(R.id.tablayout1);
        prev_item = (TabItem)findViewById(R.id.item1);
        curr_item = (TabItem)findViewById(R.id.item2);
        vpg = (ViewPager)findViewById(R.id.fragmentcontainer);
}

    @Override
    protected void onResume() {
        super.onResume();
        pageAdaptor = new PageAdaptor(getSupportFragmentManager(),tabLayout.getTabCount(), type);
        vpg.setAdapter(pageAdaptor);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpg.setCurrentItem(tab.getPosition());

                if(tab.getPosition() == 0 || tab.getPosition() == 1){
                    pageAdaptor.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpg.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}
