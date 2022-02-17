package com.example.sevaarth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Donation_Details_Org extends AppCompatActivity {
    private DatabaseReference mDatabase_donate, mDatabase_rating;
    private FirebaseUser user;
    private TextView name,email, donated_items, date_received, date_donated;
    private Switch received_status;
    private ImageView image;
    private String org_email;
    private boolean complete;
    UploadInfo image_upload;
    private RatingBar rating;
    float rating_val = 0.0f;
    private Button submit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_items);
        name = findViewById(R.id.detailfullname);
        email = findViewById(R.id.detailemailid);
        donated_items = findViewById(R.id.detailsdonated);
        date_received = findViewById(R.id.dater_val);
        date_donated = findViewById(R.id.dated_val);
        received_status = findViewById(R.id.received);
        image = findViewById(R.id.image_display);

        String key = getIntent().getStringExtra("Key");
        user = FirebaseAuth.getInstance().getCurrentUser();
        org_email = user.getEmail();
        org_email = org_email.replace(".", ",");
        mDatabase_donate = FirebaseDatabase.getInstance().getReference().child("OrgDonations");
        mDatabase_rating = FirebaseDatabase.getInstance().getReference().child("Ratings");
        mDatabase_donate.child(org_email).child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    name.setText(snapshot.child("name").getValue(String.class));
                    email.setText((snapshot.child("email").getValue(String.class)).replace(",", "."));
                    String items_donate = "\n\n";
                    if (!snapshot.child("food").getValue(String.class).equals("")){
                        items_donate += "Food: "+ snapshot.child("food").getValue(String.class) + "\n";
                    }
                    if (!snapshot.child("clothes").getValue(String.class).equals("")){
                        items_donate += "Clothes: "+ snapshot.child("clothes").getValue(String.class) + "\n";
                    }
                    if (!snapshot.child("books").getValue(String.class).equals("")){
                        items_donate += "Books: "+ snapshot.child("books").getValue(String.class) + "\n";
                    }
                    if (!snapshot.child("medicines").getValue(String.class).equals("")){
                        items_donate += "Medicines: "+ snapshot.child("medicines").getValue(String.class) + "\n";
                    }
                    if (!snapshot.child("money").getValue(String.class).equals("")){
                        items_donate += "Money: "+ snapshot.child("money").getValue(String.class) + "\n";
                    }
                    if (!snapshot.child("blood").getValue(String.class).equals("")){
                        items_donate += "Blood: "+ snapshot.child("blood").getValue(String.class) + "\n";
                    }
                    donated_items.setText(items_donate);
                    date_donated.setText(snapshot.child("date_donated").getValue(String.class));
                    date_received.setText(snapshot.child("date_received").getValue(String.class));
                    image_upload = snapshot.child("uploaded_images").getValue(UploadInfo.class);
                    if(image_upload != null) {
                        Picasso.with(Donation_Details_Org.this).load(image_upload.getUrl()).into(image);
                    }
                    complete = snapshot.child("complete").getValue(Boolean.class);
                    if (complete == false) {
                        received_status.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                received_status.setChecked(isChecked);
                                if (isChecked) {
                                    Donation_Data_Org org_data = snapshot.getValue(Donation_Data_Org.class);
                                    org_data.setComplete(true);
                                    Date date = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(date);
                                    org_data.setDate_received(formattedDate);
                                    mDatabase_donate.child(org_email).child(key).setValue(org_data);
                                    View view = LayoutInflater.from(Donation_Details_Org.this).inflate(R.layout.rating_bar, null);
                                    rating = (RatingBar) view.findViewById(R.id.ratingBar);
                                    submit = (Button) view.findViewById(R.id.submitrating);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(Donation_Details_Org.this);
                                    builder.setView(view);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                    rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                        @Override
                                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                            rating_val = rating;
                                        }
                                    });
                                    submit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Rating rating_db = new Rating(formattedDate, rating_val);
                                            mDatabase_rating.child(snapshot.child("email").getValue(String.class)).child(org_email).push().setValue(rating_db);
                                            String email = snapshot.child("email").getValue(String.class);
                                            mDatabase_rating.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int count_2 = 0, total_2 = 0;
                                                    if (snapshot.exists()){
                                                        for (DataSnapshot datasnapshot: snapshot.getChildren()){
                                                            Log.d("RUN", String.valueOf(datasnapshot));
                                                            for (DataSnapshot childsnap: datasnapshot.getChildren()){
                                                                Log.d("RUN", String.valueOf(childsnap));
                                                                Rating r = childsnap.getValue(Rating.class);
                                                                if (r.getRating() < 2){
                                                                    count_2 += 1;
                                                                }
                                                                total_2 += 1;
                                                            }
                                                        }
                                                        if (count_2/total_2 > 0.5) {
                                                            DatabaseReference user = FirebaseDatabase.getInstance().getReference().child("Users");
                                                            user.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    User user_val = snapshot.getValue(User.class);
                                                                    user_val.setEnable(0);
                                                                    user.child(email).setValue(user_val);
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError error) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                            dialog.dismiss();
                                        }
                                    });

                                }
                            }
                        });
                    }
                    else{
                        received_status.setChecked(true);
                        received_status.setClickable(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}

