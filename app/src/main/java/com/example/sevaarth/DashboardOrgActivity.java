package com.example.sevaarth;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class DashboardOrgActivity extends AppCompatActivity {

    private FloatingActionButton chat, donations, profile;
    private TextView donation_status, verification_status,showname;
    private FirebaseUser user;
    private DatabaseReference reference_user, reference_org, reference_orgdon;
    private String userKey;
    String name, email;
    private int verify_status, docs_status, details_status, complete;
    ArrayList<SliderData> sliderDataArrayList;
    SliderView sliderView;
    String url1,url2,url3;
    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;



    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    protected void displaying_status(DatabaseReference reference, String email){
        verification_status = (TextView)findViewById(R.id.verification_status);
        reference.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.d("RUN", "Entering");
                    verify_status = snapshot.child("verify").getValue(Integer.class);
                    docs_status = snapshot.child("docs_verify").getValue(Integer.class);
                    details_status = snapshot.child("details_verify").getValue(Integer.class);
                    //Toast.makeText(getApplicationContext(), verify_status, Toast.LENGTH_LONG).show();
                    if(verify_status == 1) {
                        verification_status.setText("You have been Verified!");
                    }
                    else{
                        verification_status.setText("You are not Verified! Please Complete the Details");
                    }
                }
                else{
                    verification_status.setText("Move to Profile Section to complete details!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DashboardOrgActivity.this,"Something wrong happened !",Toast.LENGTH_LONG).show();
            }
        });


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_org);
        bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.donations);
        showname = (TextView) findViewById(R.id.showname);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.donations:
                        startActivity(new Intent(DashboardOrgActivity.this, DonationActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.events:
                        startActivity(new Intent(DashboardOrgActivity.this,Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(DashboardOrgActivity.this, ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(DashboardOrgActivity.this, AboutUs.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.signout:
                        startActivity(new Intent(DashboardOrgActivity.this, LoginActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderDataArrayList = new ArrayList<>();

        sliderView = findViewById(R.id.slider);
        url1 = "https://hostinternational.org.au/wp-content/uploads/2019/04/how-a-small-donation-can-help-build-a-school-for-refugee-children-740x470.jpg";
        url2 = "https://hiring.workopolis.com/wp-content/uploads/sites/3/2016/10/iStock_32427148_SMALL.jpg";
        url3 = "https://diyaindia.org/images/events/children-heart-day/small_1511959839.jpg";
        sliderDataArrayList.add(new SliderData(url1));
        sliderDataArrayList.add(new SliderData(url2));
        sliderDataArrayList.add(new SliderData(url3));
        SliderAdapter1 adapter = new SliderAdapter1(this, sliderDataArrayList);
        sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
        sliderView.setSliderAdapter(adapter);
        sliderView.setScrollTimeInSec(3);
        sliderView.setAutoCycle(true);
        sliderView.startAutoCycle();

        user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("RUN", String.valueOf(user));
        reference_user = FirebaseDatabase.getInstance().getReference("Users");
        reference_org = FirebaseDatabase.getInstance().getReference("Organisation");
//        reference_orgdon = FirebaseDatabase.getInstance().getReference("OrgDonations");
        userKey = user.getEmail();
        Log.d("RUN", userKey);
        userKey = EncodeString(userKey);
        Log.d("RUN", userKey);
        reference_user.child(userKey).child("fullName").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
                showname.setText("Welcome "+ name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        displaying_status(reference_org, userKey);
    }
}