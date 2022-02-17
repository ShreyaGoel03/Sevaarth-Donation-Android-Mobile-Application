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
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DisplayDetails extends AppCompatActivity {

    private DatabaseReference reference_org,reference;

    private TextView textViewfullname,textViewemail,detailfullname,detailemail,detailphone;

    private  TextView add,area,items,desc,head,strength,contact;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    String encodedemail, items_req="";
    FirebaseStorage firebaseStorage;
    StorageReference databaseReference;
    ArrayList<UploadInfo> org_images;
    RecyclerView images_view;
    GalleryAdapter adapter;
    CountDownTimer countDownTimer;
    private int verify_status, docs_status, details_status;
    FusedLocationProviderClient fusedLocationProviderClient;
    String CurrentLocation,DestLocation;
    private ImageButton call,chat,donate, locate;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_details);
        detailfullname = (TextView) findViewById(R.id.detailfullname);
        detailemail = (TextView) findViewById(R.id.detailemailid);
        add=findViewById(R.id.detailaddress);
        area=findViewById(R.id.detailarea);
        desc=findViewById(R.id.detaildesc);
        head=findViewById(R.id.detailhead);
        items = findViewById(R.id.detailitem);
        strength=findViewById(R.id.detailstrength);
        detailphone=findViewById(R.id.detailphone);
        images_view = findViewById(R.id.rec_view);
        String fullname = getIntent().getStringExtra("NGO Name");
        String emailid = getIntent().getStringExtra("Email id");
        String num;

        detailfullname.setText(fullname);
        detailemail.setText(emailid);


//        call=findViewById(R.id.call);

        call=findViewById(R.id.call);
        locate = findViewById(R.id.imageButton);
        chat = findViewById(R.id.imageButton2);
        donate = findViewById(R.id.imageButton3);
        encodedemail= EncodeString(emailid);
        reference_org = FirebaseDatabase.getInstance().getReference("Organisation");
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseStorage.getReference().child(encodedemail);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        reference_org = FirebaseDatabase.getInstance().getReference("Organisation");

        reference_org.child(encodedemail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    DestLocation= snapshot.child("address").getValue().toString();

                }
                else {
                    Toast.makeText(DisplayDetails.this, DestLocation+ " details not found",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent3= new Intent(DisplayDetails.this,CallActivity.class);
//                startActivity(intent3);
                phone = detailphone.getText().toString();
                if(phone.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Number !",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(isPermissionGranted()){
                        String s = "tel:" + phone;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                    }
                }



            }
        });

        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(DisplayDetails.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    getExactLocation();
                } else {
                    //when permission denied
                    ActivityCompat.requestPermissions(DisplayDetails.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
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

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DisplayDetails.this, DonateDetails2.class);
                intent.putExtra("Email id",emailid);
                startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = detailphone.getText().toString();
                if(num.length()==10){
                    num = "91"+num;
                }
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

        reference_org.child(encodedemail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              //  Toast.makeText(DisplayDetails.this,snapshot.child("address").getValue(String.class),Toast.LENGTH_LONG).show();
                   // snapshot.getValue(Org_Details.class);
                //Toast.makeText(DisplayDetails.this,details.area,Toast.LENGTH_LONG).show();
                if(snapshot.exists()){
                    String address = " " + snapshot.child("address").getValue(String.class) + "  " + snapshot.child("city").getValue(String.class) + "  " + snapshot.child("state").getValue(String.class);
                    add.setText(address);
                    items_req = " ";
                    if (snapshot.child("food").getValue(Integer.class) == 1){
                        items_req += " Food,  ";
                    }
                    if (snapshot.child("clothes").getValue(Integer.class) == 1){
                        items_req += " Clothes, ";
                    }
                    if (snapshot.child("medicine").getValue(Integer.class) == 1){
                        items_req += " Medicine, ";
                    }
                    if (snapshot.child("book").getValue(Integer.class) == 1){
                        items_req += " Books, ";
                    }
                    if (snapshot.child("blood").getValue(Integer.class) == 1){
                        items_req += " Blood, ";
                    }
                    if (snapshot.child("money").getValue(Integer.class) == 1){
                        items_req += " Money, ";
                    }
                    StringBuffer stringBuffer = new StringBuffer(items_req);
                    stringBuffer.deleteCharAt(stringBuffer.length()-1);
                    String areaint= snapshot.child("area").getValue().toString();

                    area.setText(areaint + " acres");
                    items.setText(stringBuffer);
                    detailphone.setText(snapshot.child("phone").getValue().toString());
                    desc.setText(snapshot.child("desc").getValue(String.class));
                    head.setText(snapshot.child("head").getValue(String.class));
                    strength.setText(snapshot.child("strength").getValue().toString() + " people");
                    desc.setText(snapshot.child("desc").getValue(String.class));
                }
                else {
                    Toast.makeText(DisplayDetails.this, fullname+ " details not found",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        org_images = new ArrayList<>();
        images_view.setHasFixedSize(true);
        images_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new GalleryAdapter(this,org_images);
        images_view.setAdapter(adapter);
        fetch_images();

    }

    private void fetch_images(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Organisation").child(encodedemail);
        Log.d("RUN", "Ref "+ reference);
        reference.child("images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                org_images.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    UploadInfo upload = dataSnapshot.getValue(UploadInfo.class);
                    org_images.add(upload);
                    adapter = new GalleryAdapter(DisplayDetails.this,org_images);
                    images_view.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    String s = "tel:" + phone;
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse(s));
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
            }

        }

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
                    Geocoder geocoder = new Geocoder(DisplayDetails.this, Locale.getDefault());

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