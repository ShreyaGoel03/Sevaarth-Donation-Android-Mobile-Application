package com.example.sevaarth;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DonorActivity2 extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private Button donate,showdetails,showevents,Location,displayevents,chat;
    FusedLocationProviderClient fusedLocationProviderClient;
    String CurrentLocation,DestLocation;
    String encodedemail;
    String eml;
    private DatabaseReference reference_org;
    FloatingActionButton signout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor2);
        donate=(Button) findViewById(R.id.donate);
        showdetails=(Button) findViewById(R.id.showdetails);
        showevents=(Button) findViewById(R.id.showevents);
        signout=(FloatingActionButton) findViewById(R.id.signout);
        Location=findViewById(R.id.Show_Location);
        displayevents=findViewById(R.id.displayevents);
        chat = findViewById(R.id.chat);

        displayevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Fragment fragment=new FragmentEvents();
//
//
//                FragmentManager fragmentManager=getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.frameLayoutevents,fragment);
//                fragmentTransaction.commit();

                Intent intent=new Intent(DonorActivity2.this, Dashboard.class);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = "919654376989";
                Log.d("RUN", "Send message");
                String text = "Hello";
                try {
                    Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                    sharingIntent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + num + "&text" + text));
                    startActivity(Intent.createChooser(sharingIntent, "Chat with Organisation!"));
                }
                catch(Exception e){
                    Intent I =new Intent(Intent.ACTION_VIEW);
                    I.setData(Uri.parse("smsto:"));
                    I.putExtra("address", num);
                    startActivity(Intent.createChooser(I, "Chat with Organisation!"));
                }
            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        String fname=getIntent().getStringExtra("NGO Name");
         eml=getIntent().getStringExtra("Email id");

        encodedemail= EncodeString(eml);
        reference_org = FirebaseDatabase.getInstance().getReference("Organisation");

        reference_org.child(encodedemail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  Toast.makeText(DisplayDetails.this,snapshot.child("address").getValue(String.class),Toast.LENGTH_LONG).show();
                // snapshot.getValue(Org_Details.class);
                //Toast.makeText(DisplayDetails.this,details.area,Toast.LENGTH_LONG).show();
                if(snapshot.exists()){
                    DestLocation= snapshot.child("address").getValue().toString();

                }
                else {
                    Toast.makeText(DonorActivity2.this, DestLocation+ " details not found",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(DonorActivity2.this, LoginActivity.class));
            }
        });

        showevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(DonorActivity2.this, DisplayEvents.class);
                intent3.putExtra("Email id",eml);
                startActivity(intent3);
            }
        });

        showdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(DonorActivity2.this, DisplayDetails.class);
                intent2.putExtra("ngo",fname);
                intent2.putExtra("Email id",eml);
              //  Toast.makeText(DonorActivity2.this,fname,Toast.LENGTH_LONG).show();
                startActivity(intent2);
            }
        });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DonorActivity2.this, DonateDetails2.class);
                intent.putExtra("Email id",eml);
                startActivity(intent);
            }
        });
        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(DonorActivity2.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getExactLocation();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(DonorActivity2.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }


//                String source = CurrentLocation.toString().trim();
//                String destination = CurrentLocation.toString().trim();
//
//                if (source.equals("") && destination.equals("")) {
//                    Toast.makeText(getApplicationContext(), "Enter both locations ! ", Toast.LENGTH_SHORT).show();
//                } else {
//                    displayTrack(source, destination);
//                }

            }
        });

    }

    private void displayTrack(String source, String destination) {
        // if the device does not have a map installed , redirect it to playstore
        try
        {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + source + "/" + destination);
            Intent intent = new  Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        catch(ActivityNotFoundException e)
        {
            // when google map is not installed
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void getExactLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    Geocoder geocoder = new Geocoder(DonorActivity2.this, Locale.getDefault());

                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        CurrentLocation=addressList.get(0).getAddressLine(0);

                        Log.d("run",CurrentLocation);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                String source = CurrentLocation.toString().trim();
                String destination = DestLocation.toString().trim();

                if (source.equals("") && destination.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter both locations ! ", Toast.LENGTH_SHORT).show();
                } else {
                    displayTrack(source, destination);
                }
            }
        });
    }

    private static String EncodeString(String string) {
        return (string.replace(".", ","));
    }

}