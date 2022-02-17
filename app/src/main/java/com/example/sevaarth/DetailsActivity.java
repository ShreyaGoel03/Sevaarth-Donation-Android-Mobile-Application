package com.example.sevaarth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class DetailsActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private EditText head_org, area_org, address_org, strength_org, desc_org, phone_org, city_org;
    private CheckBox food_org, clothes_org, books_org, money_org, medicines_org, blood_org, equipments_org;
    private Button save, upload_pic;
    private Spinner sp;//c
    private FirebaseUser user;
    private DatabaseReference mDataBase_org;
    private String userKey;
    private Map<String, UploadInfo> images = null;
    private ArrayAdapter<String> loc_adaptor;
    int atleastOneSelected = 0, details_filled=0,docs_uploaded=0, docs_status=0, image_status=0, details_status=0;


    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_details);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Organisation");
        head_org = (EditText) findViewById(R.id.head_view);
        city_org = (EditText) findViewById(R.id.city_view);
        area_org = (EditText) findViewById(R.id.area_view);
        address_org = (EditText) findViewById(R.id.address_view);
        strength_org = (EditText) findViewById(R.id.strength_view);
        desc_org = (EditText) findViewById(R.id.desc_view);
        phone_org = (EditText)findViewById(R.id.phone_view) ;
        food_org = (CheckBox)findViewById(R.id.food_cb);
        clothes_org = (CheckBox)findViewById(R.id.clothes_cb);
        books_org = (CheckBox)findViewById(R.id.books_cb);
        money_org = (CheckBox)findViewById(R.id.money_cb);
        medicines_org = (CheckBox)findViewById(R.id.medicine_cb);
        blood_org = (CheckBox)findViewById(R.id.blood_cb);
        sp = (Spinner)findViewById(R.id.state_spinner);
        loc_adaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.state_names));
        loc_adaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(loc_adaptor);//c
        save = (Button)findViewById(R.id.save_btn);
        upload_pic = (Button)findViewById(R.id.photo_btn);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDataBase_org = FirebaseDatabase.getInstance().getReference("Organisation");
        userKey = EncodeString(user.getEmail());
        mDataBase_org.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    head_org.setText(dataSnapshot.child("head").getValue(String.class));
                    area_org.setText(String.valueOf(dataSnapshot.child("area").getValue(Integer.class)));
                    address_org.setText(dataSnapshot.child("address").getValue(String.class));
                    strength_org.setText(String.valueOf(dataSnapshot.child("strength").getValue(Integer.class)));
                    desc_org.setText(dataSnapshot.child("desc").getValue(String.class));
                    phone_org.setText(dataSnapshot.child("phone").getValue(String.class));
                    city_org.setText(dataSnapshot.child("city").getValue(String.class));
                    if (dataSnapshot.child("food").getValue(Integer.class) == 1){
                        food_org.setChecked(true);
                    }
                    if (dataSnapshot.child("clothes").getValue(Integer.class) == 1){
                        clothes_org.setChecked(true);
                    }
                    if (dataSnapshot.child("medicine").getValue(Integer.class) == 1){
                        medicines_org.setChecked(true);
                    }
                    if (dataSnapshot.child("book").getValue(Integer.class) == 1){
                        books_org.setChecked(true);
                    }
                    if (dataSnapshot.child("blood").getValue(Integer.class) == 1){
                        blood_org.setChecked(true);
                    }
                    if (dataSnapshot.child("money").getValue(Integer.class) == 1){
                        money_org.setChecked(true);
                    }
                    String state = dataSnapshot.child("state").getValue(String.class);//c
                    sp.setSelection(loc_adaptor.getPosition(state));//c
                    image_status  = dataSnapshot.child("image_status").getValue(Integer.class);
                    docs_uploaded = dataSnapshot.child("docs_status").getValue(Integer.class);
                    details_filled = dataSnapshot.child("details_status").getValue(Integer.class);
                    docs_status = dataSnapshot.child("docs_verify").getValue(Integer.class);
                    details_status = dataSnapshot.child("details_verify").getValue(Integer.class);
                    images = (Map<String, UploadInfo>)dataSnapshot.child("images").getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent image = new Intent(v.getContext(),UploadActivity.class);
                Intent typeIntent = new Intent(DetailsActivity.this,UploadActivity.class);
                int type = 1;
                typeIntent.putExtra("Type", type);
                startActivityForResult(typeIntent,0);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int food=0, clothes=0, books=0, money=0, medicines=0, blood=0, equipments=0;
                String state = sp.getSelectedItem().toString();//c
                String phone = phone_org.getText().toString();
                String head = head_org.getText().toString();
                String area_str = area_org.getText().toString();
                String city = city_org.getText().toString();
                int area=0, strength=0;
                if(area_str.length() != 0){
                    try{
                        area = Integer.parseInt(area_org.getText().toString());
                    }catch (NumberFormatException e){
                        Toast.makeText(DetailsActivity.this,"Area is a numeric field!", Toast.LENGTH_LONG).show();
                    }
                }
                String address = address_org.getText().toString();
                String strength_str = strength_org.getText().toString();
                if(strength_str.length() != 0){
                    try {
                        strength = Integer.parseInt(strength_org.getText().toString());
                    }catch (NumberFormatException e){
                        Toast.makeText(DetailsActivity.this,"Strength is a numeric field!", Toast.LENGTH_LONG).show();
                    }
                }
                String desc = desc_org.getText().toString();
                if(food_org.isChecked()){
                    atleastOneSelected = 1;
                    food = 1;
                }
                if(clothes_org.isChecked()){
                    atleastOneSelected = 1;
                    clothes = 1;
                }
                if(books_org.isChecked()){
                    atleastOneSelected = 1;
                    books = 1;
                }
                if(money_org.isChecked()){
                    atleastOneSelected = 1;
                    money = 1;
                }
                if(medicines_org.isChecked()){
                    atleastOneSelected = 1;
                    medicines = 1;
                }
                if(blood_org.isChecked()){
                    atleastOneSelected = 1;
                    blood = 1;
                }

                if(head.length()==0 || area_str.length()==0 || address.length()==0 || strength_str.length()==0 || desc.length()==0 || phone.length() == 0 || atleastOneSelected==0 || image_status==0){
                    Toast.makeText(DetailsActivity.this,"You have not completed your profile!", Toast.LENGTH_SHORT).show();
                    details_filled = 0;
                }
                else{
                    details_filled = 1;
                }
                //images = mDatabase.child(userKey).child("images")
                Log.d("RUN", "Images 2 "+String.valueOf(images));
                Org_Details details = new Org_Details(head,area,address,city, strength,details_status,docs_status, food,clothes,medicines,books,blood,money,phone, desc,state, images, docs_uploaded, image_status, details_filled);//c
                mDatabase.child(userKey).setValue(details);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Toast.makeText(DetailsActivity.this,"Details Updated!", Toast.LENGTH_LONG).show();
                    }
                }, 2000);

            }

        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){
            if(data.getIntExtra("RES",0) == 1){
                Log.d("RUN", "value details " );
                image_status = 1;
                mDatabase.child(userKey).child("image_status").setValue(image_status);
            }
        }
    }
}
