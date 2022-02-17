package com.example.sevaarth;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Donor extends AppCompatActivity {

    private Button check, events;
    private ArrayList<String> listData;
    private DataAdapter adapter;
    private FirebaseUser user;
    private String userId;
    private String userType;
    public String useremailid;
    private String code= "Help Needy one's like me! Sign up on Sevaarth to make difference . use this referrel code,xgv234";
    private int Foodval,Bookval,Clothesval,Bloodval,Medicineval,Moneyval;
    private int org_food,org_book,org_clothes,org_blood,org_medicine,org_money;
    private  String email, encodedemail,name,state;
    DatabaseReference reference_org;
    private int count=0;
    private ArrayAdapter<String> loc_adaptor;//c
    Spinner sp;

    NavigationView nav;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    //    Toolbar supportActionBar;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;




    private CheckBox Food,Book,Clothes,Blood,Medicine,Money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor);
        Food= findViewById(R.id.foodCheckboxes);
        Book= findViewById(R.id.bookCheckboxes);
        Clothes= findViewById(R.id.clotheCheckboxes);
        Blood= findViewById(R.id.bloodCheckboxes);
        Medicine= findViewById(R.id.medicineCheckboxes);
        Money= findViewById(R.id.moneyCheckboxes);
        useremailid = getIntent().getStringExtra("useremailid");
        check=(Button) findViewById(R.id.check);
        sp = (Spinner)findViewById(R.id.state_spinner1);//c
        loc_adaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.state_names));
        loc_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//c
        sp.setAdapter(loc_adaptor);//c

//                Fragment fragment=new FragmentDonor();
//                fragment.setArguments(bundle);
//
//                FragmentManager fragmentManager=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayout,fragment);
//                fragmentTransaction.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                state = sp.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_Food = String.valueOf(Food.isChecked());
                String s_Book = String.valueOf(Book.isChecked());
                String s_Clothes = String.valueOf(Clothes.isChecked());
                String s_Blood = String.valueOf(Blood.isChecked());
                String s_Medicine = String.valueOf(Medicine.isChecked());
                String s_Money = String.valueOf(Money.isChecked());
                Log.d("RUN", "Food" + String.valueOf(Food.isChecked()));
                Log.d("RUN", "Book" + String.valueOf(Book.isChecked()));
                Log.d("RUN", "Clothes" + String.valueOf(Clothes.isChecked()));
                Log.d("RUN", "Medicine" + String.valueOf(Medicine.isChecked()));
                if (s_Food.equals("false")) {
                    Foodval = 0;
                } else {
                    Foodval = 1;
                }

                if (s_Book.equals("false")) {
                    Bookval = 0;
                } else {
                    Bookval = 1;
                }

                if (s_Clothes.equals("false")) {
                    Clothesval = 0;

                } else {
                    Clothesval = 1;
                }

                if (s_Blood.equals("false")) {
                    Bloodval = 0;
                } else {
                    Bloodval = 1;
                }
                if (s_Medicine.equals("false")) {
                    Medicineval = 0;

                } else {
                    Medicineval = 1;
                }

                if (s_Money.equals("false")) {
                    Moneyval = 0;
                } else {
                    Moneyval = 1;
                }
                reference_org = FirebaseDatabase.getInstance().getReference("Organisation");
                reference_org.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            count = (int) snapshot.getChildrenCount();
                            listData = new ArrayList<>();
                            int count_org = 0, flag = 0;
                            Log.d("RUN", "State " + String.valueOf(state));
                            for (DataSnapshot dbsnapshot : snapshot.getChildren()) {
                                Log.d("RUN", "Count "+ String.valueOf(count_org));
                                count_org += 1;
                                String cur_state = dbsnapshot.child("state").getValue(String.class);
                                Log.d("RUN", "Current State " + String.valueOf(cur_state));
                                int food = dbsnapshot.child("food").getValue(Integer.class);
                                int book = dbsnapshot.child("book").getValue(Integer.class);
                                int clothes = dbsnapshot.child("clothes").getValue(Integer.class);
                                int blood = dbsnapshot.child("blood").getValue(Integer.class);
                                int medicine = dbsnapshot.child("medicine").getValue(Integer.class);
                                int money = dbsnapshot.child("money").getValue(Integer.class);
                                int verify = dbsnapshot.child("verify").getValue(Integer.class);
                                Log.d("run", String.valueOf(food));
                                Log.d("run", String.valueOf(book));
                                Log.d("count", String.valueOf(count_org));
                                int finalCount_org = count_org;
                                if ((state.equals(cur_state)) && ((Foodval == food && Foodval == 1) || (Bookval == book && Bookval == 1) ||
                                        (Clothesval == clothes && Clothesval == 1) || (Bloodval == blood && Bloodval == 1) ||
                                        (Medicineval == medicine && Medicineval == 1) || (Moneyval == money && Moneyval == 1)) && (verify == 1)) {
                                    Log.d("RUN", "Entered");
                                    flag = 1;
                                    Log.d("RUN", "Final Count outside " + String.valueOf(finalCount_org));
                                    listData.add(String.valueOf(dbsnapshot.getKey()));
                                }
                                if (finalCount_org == count ){
                                    if (flag == 1){
                                        Intent orgs = new Intent(Donor.this, DisplayOrgs.class);
                                        orgs.putExtra("Name", listData);
                                        startActivity(orgs);
                                    }
                                    else{
                                        Toast.makeText(Donor.this, "No Organisations Available", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setSelectedItemId(R.id.donations);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.hostevents:
                        startActivity(new Intent(Donor.this, DisplayEvents.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.showevents:
                        startActivity(new Intent(Donor.this,Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;


                    case R.id.about:
                        startActivity(new Intent(Donor.this, AboutUs.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.faq:
                        startActivity(new Intent(Donor.this, FAQs.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.signout:
                        startActivity(new Intent(Donor.this, LoginActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

    }

    private static String EncodeString(String string) {
        return (string.replace(".", ","));
    }

}