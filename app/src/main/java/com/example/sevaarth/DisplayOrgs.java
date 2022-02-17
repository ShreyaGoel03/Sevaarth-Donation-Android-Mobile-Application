package com.example.sevaarth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayOrgs extends AppCompatActivity {
    private ArrayList<String> listData;
    private ArrayList<Data> org_details;
    private RecyclerView rv;
    private DataAdapter adapter;
    private FirebaseUser user;
    private String userId;
    private String userType;
    int count = 0;
    DatabaseReference reference_org;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_orgs);
        rv= (RecyclerView) findViewById(R.id.recycler1);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData = (ArrayList<String>) getIntent().getSerializableExtra("Name");
        Log.d("RUN","SIZE" + String.valueOf(listData.size()));
        Log.d("RUN","DATA" + String.valueOf(listData));
        org_details = new ArrayList<>();
        final DatabaseReference nm = FirebaseDatabase.getInstance().getReference("Users");
        for (int i=0; i<listData.size(); i++) {
            Log.d("RUN", String.valueOf(listData.get(i)));
            Log.d("RUN", String.valueOf(nm.child(String.valueOf(listData.get(i)))));
            nm.child(String.valueOf(listData.get(i))).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot npsnapshot) {

                    Log.d("RUN", String.valueOf(npsnapshot));
                    Data l = npsnapshot.getValue(Data.class);
                    Log.d("RUN", String.valueOf(l));
                    org_details.add(l);
                    count += 1;
                    if (count == listData.size()) {
                        adapter = new DataAdapter(org_details);
                        rv.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }


}

