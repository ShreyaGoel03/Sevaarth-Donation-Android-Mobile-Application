package com.example.sevaarth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FAQs extends AppCompatActivity {

    ExpandableListView expandableListView;
    List<String> listGroup;
    HashMap<String,List<String>> listItem;
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_qs);

        expandableListView = findViewById(R.id.expandableView);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();

        adapter = new MainAdapter(this,listGroup,listItem);
        expandableListView.setAdapter(adapter);

        initListData();

    }

    private void initListData() {

        listGroup.add(getString(R.string.Q1));
        listGroup.add(getString(R.string.Q2));
        listGroup.add(getString(R.string.Q3));
        listGroup.add(getString(R.string.Q4));
        listGroup.add(getString(R.string.Q5));
        listGroup.add(getString(R.string.Q6));
        listGroup.add(getString(R.string.Q7));
        listGroup.add(getString(R.string.Q10));
        listGroup.add(getString(R.string.Q11));
        listGroup.add(getString(R.string.Q12));

        String[] array;

        List<String> list1 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q1);
        for(String item:array)
        {
            list1.add(item);
        }

        List<String> list2 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q2);
        for(String item:array)
        {
            list2.add(item);
        }

        List<String> list3 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q3);
        for(String item:array)
        {
            list3.add(item);
        }

        List<String> list4 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q4);
        for(String item:array)
        {
            list4.add(item);
        }

        List<String> list5 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q5);
        for(String item:array)
        {
            list5.add(item);
        }

        List<String> list6 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q6);
        for(String item:array)
        {
            list6.add(item);
        }

        List<String> list7 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q7);
        for(String item:array)
        {
            list7.add(item);
        }

        List<String> list10 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q10);
        for(String item:array)
        {
            list10.add(item);
        }

        List<String> list11 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q11);
        for(String item:array)
        {
            list11.add(item);
        }

        List<String> list12 = new ArrayList<>();
        array = getResources().getStringArray(R.array.Q12);
        for(String item:array)
        {
            list12.add(item);
        }


        listItem.put(listGroup.get(0),list1);
        listItem.put(listGroup.get(1),list2);
        listItem.put(listGroup.get(2),list3);
        listItem.put(listGroup.get(3),list4);
        listItem.put(listGroup.get(4),list5);
        listItem.put(listGroup.get(5),list6);
        listItem.put(listGroup.get(6),list7);
        listItem.put(listGroup.get(7),list10);
        listItem.put(listGroup.get(8),list11);
        listItem.put(listGroup.get(9),list12);

        adapter.notifyDataSetChanged();

    }
}