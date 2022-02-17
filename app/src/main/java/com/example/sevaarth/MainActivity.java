package com.example.sevaarth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private ViewPager mySlideViewPager;
    private LinearLayout myLinearLayout;
    private SliderAdapter mySlideAdapter;

    private TextView[] myDots;

    private Button myNextButton;
    private Button myPrevButton;

    private int myCurrentPage;


    private PreferenceManager PreferenceManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Checking for first time launch - before calling setContentView()
        PreferenceManager  = new PreferenceManager (this);
        if (!PreferenceManager .isFirstTimeLaunch()) {
            startHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_main);


        mySlideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        myLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_id);

        myNextButton = (Button) findViewById(R.id.nextButton);
        myPrevButton = (Button) findViewById(R.id.prevButton);

        mySlideAdapter = new SliderAdapter(this);
        mySlideViewPager.setAdapter(mySlideAdapter);

        addDots(0);
        mySlideViewPager.addOnPageChangeListener(myViewListener);



        myNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // mySlideViewPager.setCurrentItem(myCurrentPage + 1);
                if (myCurrentPage == mySlideViewPager.getAdapter().getCount() - 1) {

                    startHomeScreen();
                } else {
                    mySlideViewPager.setCurrentItem(myCurrentPage + 1);

                }

            }
        });

        myPrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mySlideViewPager.setCurrentItem(myCurrentPage - 1);
                startHomeScreen();

            }
        });



        }

    private void startHomeScreen() {

        PreferenceManager.setFirstTimeLaunch(false);
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

    public void addDots(int pos) {

        myDots = new TextView[3];
        myLinearLayout.removeAllViews();

        for (int i = 0; i < myDots.length; i++)
        {
            myDots[i] = new TextView(this);
            myDots[i].setText(Html.fromHtml("&#8226"));
            myDots[i].setTextSize(35);
            myDots[i].setTextColor(getResources().getColor(R.color.transparent_white));

            myLinearLayout.addView(myDots[i]);

        }
        if(myDots.length > 0)
        {
            myDots[pos].setTextColor(getResources().getColor(R.color.black));
        }
    }

    ViewPager.OnPageChangeListener myViewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);

            myCurrentPage=position;


           if(position == 0)
            {
                myNextButton.setEnabled(true);
                myPrevButton.setEnabled(false);
                myPrevButton.setVisibility(View.VISIBLE);
                myNextButton.setText("Next");
                myPrevButton.setText("Skip");
            }
            else if(position == myDots.length - 1 )
            {
                myNextButton.setEnabled(true);
                myPrevButton.setEnabled(true);
                myPrevButton.setVisibility(View.VISIBLE);
                myNextButton.setText("Start");
                myPrevButton.setText("Skip");
            }
            else
            {
                myNextButton.setEnabled(true);
                myPrevButton.setEnabled(true);
                myPrevButton.setVisibility(View.VISIBLE);
                myNextButton.setText("Next");
                myPrevButton.setText("Skip");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };



}