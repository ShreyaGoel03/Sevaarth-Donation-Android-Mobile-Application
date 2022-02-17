package com.example.sevaarth;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private TextView detailsView;
    private String message;
    String userencodedmail;
    private FirebaseAuth mAuth;
    private DatabaseReference reference_event;
    FirebaseUser user;
    String message1;
    private DatabaseReference mDatabase;
    private List<Events> listData;
    private RecyclerView rv;
    private EventsAdapter adapter;
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        //detailsView = findViewById(R.id.showDetails);

        rv=(RecyclerView)findViewById(R.id.recycler2);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        listData=new ArrayList<>();

        final DatabaseReference nm= FirebaseDatabase.getInstance().getReference("Events");


        user = FirebaseAuth.getInstance().getCurrentUser();
        String userkey = user.getEmail();
        userencodedmail=EncodeString(userkey);
//        reference_event = FirebaseDatabase.getInstance().getReference("Events");
//
//        reference_event.child(userencodedmail).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    message1=snapshot.child("event_Details").getValue().toString();
//                   // detailsView.setText(message1);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        Events l=npsnapshot.getValue(Events.class);
                        listData.add(l);
                    }
                    adapter=new EventsAdapter(listData);
                    rv.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Intent intent = getIntent();
        //message = intent.getStringExtra("MESSAGE");



    }
}