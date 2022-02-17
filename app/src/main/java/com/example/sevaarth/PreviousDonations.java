package com.example.sevaarth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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


public class PreviousDonations extends Fragment {
    private RecyclerView mlistRecyclerView;
    View view;
    private DetailsAdapter adapter;
    private FirebaseUser user;
    private DatabaseReference mDataBase_user;
    private String userKey,email;
    private ArrayList<Donation_Data_Org> previousdonations;
    private DatabaseReference mDatabase;
    private int type;
    public PreviousDonations(int type) {
        this.type = type;
    }
    public PreviousDonations(){

    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    protected void displaying_status(DatabaseReference reference, String email){
        reference.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("RUN", "pehle previous " +String.valueOf(previousdonations.size()));
                if (snapshot.exists()) {
                    for (DataSnapshot childsnapshot : snapshot.getChildren()) {
                        Donation_Data_Org l = childsnapshot.getValue(Donation_Data_Org.class);
                        boolean complete = l.getComplete();
                        if (complete == true){
                            previousdonations.add(l);
                        }
                    }
                    if (previousdonations.size() == 0){
                        Toast.makeText(getActivity(), "No Previous Donations!", Toast.LENGTH_SHORT).show();
                    }

                    Log.d("RUN", "after previous " +String.valueOf(previousdonations.size()));
                    adapter = new DetailsAdapter(previousdonations, 0);
                    mlistRecyclerView.setAdapter(adapter);
                }
                else{
                  //  Donation_Data_Org l = new Donation_Data_Org("There are no ", "Previous Donations", "right now", 0);
                  //  previousdonations.add(l);
                 //   adapter = new DetailsAdapter(previousdonations);
                  //  mlistRecyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void CreateList(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("OrgDonations");
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDataBase_user = FirebaseDatabase.getInstance().getReference("Users");
        email = EncodeString(user.getEmail());
        displaying_status(mDatabase,email);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.previousdonations, container, false);
        mlistRecyclerView = (RecyclerView) view.findViewById(R.id.recprevdonations);
        mlistRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.d("RUN", "Initalise previous");
        previousdonations = new ArrayList<>();
        CreateList();
        return view;
    }
}
