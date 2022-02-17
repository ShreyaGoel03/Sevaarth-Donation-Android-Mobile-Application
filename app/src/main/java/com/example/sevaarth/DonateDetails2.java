package com.example.sevaarth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DonateDetails2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText donorName,moneyQuantity, foodQuantity, clothesQuantity, booksQuantity, medicinesQuantity;
    private CheckBox foodCheck,clothesCheck,booksCheck,medicinesCheck,moneyCheck,bloodCheck;
    private Boolean hasFoodCheck,hasClothesCheck,hasBooksCheck,hasMedicinesCheck,hasMoneyCheck,hasBloodCheck;
    private String finalBloodQuantity,finalBloodType,finalDonorName,finalMoneyQuantity;
    String encodedemail, donoremail;
    String orgname;
    private FirebaseUser user;
    private DatabaseReference mDataBase_user, mDatabase_donate;
    private String finalFoodQuantity="",finalClothesQuantity="",finalBooksQuantity="",finalMedicinesQuantity="";
    private File pic;
    String emailid;
    FirebaseStorage firebaseStorage;
    StorageReference databaseReference;
    private ImageView myImage;
    private Button uploadButton;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private DatabaseReference reference_org1;
    private int imageCount = 0, flag = 0;
    private String message="";
    Uri selectImageUri;
    private UploadInfo uploadInfo = null;
    private String email, fileName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_details_2);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        donoremail = user.getEmail();
        donoremail = EncodeString(donoremail);
        myImage = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadImage);
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseStorage.getReference().child(donoremail);
        emailid = getIntent().getStringExtra("Email id");
        mDatabase_donate = FirebaseDatabase.getInstance().getReference().child("OrgDonations");

        //handling upload button click
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking runtime permission
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        // permission not granted so we will request for that
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        // showing popup for runtime permission
                        requestPermissions(permissions,PERMISSION_CODE);
                    }
                    else
                    {
                        //permission already granted
                        pickImageFromGallery();
                    }
                }
                else
                {
                    // system os is less than marshmallow
                    pickImageFromGallery();
                }
            }
        });

        Spinner typeSpinner = findViewById(R.id.bloodType);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.bloodType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);

        Spinner quantitySpinner = findViewById(R.id.bloodQuantity);
        ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(this, R.array.bloodQuantity, android.R.layout.simple_spinner_item);
        quantityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantitySpinner.setAdapter(quantityAdapter);
        quantitySpinner.setOnItemSelectedListener(this);

        encodedemail= EncodeString(emailid);

        reference_org1 = FirebaseDatabase.getInstance().getReference("Users");
        reference_org1.child(encodedemail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //  Toast.makeText(DisplayDetails.this,snapshot.child("address").getValue(String.class),Toast.LENGTH_LONG).show();
                // snapshot.getValue(Org_Details.class);
                //Toast.makeText(DisplayDetails.this,details.area,Toast.LENGTH_LONG).show();
                if(snapshot.exists()){
                    orgname= snapshot.child("fullName").getValue().toString();

                }
                else {
                    Toast.makeText(DonateDetails2.this, orgname+ " details not found",Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });


    }

    private  static String EncodeString(String emailid) {
        return (emailid.replace(".", ","));
    }

    private void pickImageFromGallery() {
        //intent to pick image from gallery
        Intent intent  = new Intent(Intent.ACTION_PICK);
        intent.setType("image/pic");
//        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // handle result of picked image

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE)
        {
            selectImageUri = data.getData();
            // setting image to image view
            myImage.setImageURI(selectImageUri);
            flag = 1;

        }
    }

    //handling result of runtime permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case PERMISSION_CODE:
            {
                if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    pickImageFromGallery();
                }
                else
                {
                    //permission was denied
                    Toast.makeText(this,"Permission got denied !",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("1 pint") || text.equals("2 pint")) {
//            Toast.makeText(parent.getContext(), "Quantity : " + text, Toast.LENGTH_SHORT).show();
            finalBloodQuantity=text;
        }
        if(text.equals("A") || text.equals("B") || text.equals("AB") || text.equals("O")) {
//            Toast.makeText(parent.getContext(), "Type : " + text, Toast.LENGTH_SHORT).show();
            finalBloodType=text;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @SuppressLint("IntentReset")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void donate(View view) {
        donorName =  findViewById(R.id.donorName);
        foodQuantity = findViewById(R.id.foodQuantity);
        clothesQuantity = findViewById(R.id.clothesQuantity);
        booksQuantity = findViewById(R.id.booksQuantity);
        medicinesQuantity = findViewById(R.id.medicinesQuantity);
        moneyQuantity =  findViewById(R.id.moneyQuantity);

        finalDonorName = donorName.getText().toString();
        finalMoneyQuantity = moneyQuantity.getText().toString();

        foodCheck =  findViewById(R.id.foodCheckbox);
        clothesCheck =  findViewById(R.id.clothesCheckbox);
        booksCheck =  findViewById(R.id.booksCheckbox);
        medicinesCheck = findViewById(R.id.medicinesCheckbox);
        moneyCheck =  findViewById(R.id.moneyCheckbox);
        bloodCheck =  findViewById(R.id.bloodCheckbox);

        hasFoodCheck = foodCheck.isChecked();
        hasClothesCheck = clothesCheck.isChecked();
        hasBooksCheck = booksCheck.isChecked();
        hasMedicinesCheck = medicinesCheck.isChecked();
        hasMoneyCheck = moneyCheck.isChecked();
        hasBloodCheck = bloodCheck.isChecked();

        finalFoodQuantity = foodQuantity.getText().toString();
        finalClothesQuantity = clothesQuantity.getText().toString();
        finalBooksQuantity = booksQuantity.getText().toString();
        finalMedicinesQuantity = medicinesQuantity.getText().toString();

//        if(hasFoodCheck==false)
//            finalFoodQuantity="";
//        else{
//            finalFoodQuantity = foodQuantity.getText().toString();
//        }
//        if(hasClothesCheck==false)
//            finalClothesQuantity="";
//        else{
//            finalClothesQuantity = clothesQuantity.getText().toString();
//        }
//        if(hasBooksCheck==false)
//            finalBooksQuantity="";
//        else{
//            finalBooksQuantity = booksQuantity.getText().toString();
//        }
//        if(hasMedicinesCheck==false)
//            finalMedicinesQuantity="";
//        else{
//            finalMedicinesQuantity = medicinesQuantity.getText().toString();
//        }
//        if(hasMoneyCheck==false)
//            finalMoneyQuantity="";
//        else{
//
//        }
//        if(hasBloodCheck==false)
//        {
//            finalBloodQuantity="";
//            finalBloodType="";
//        }
//
//        message =   "\nName : " + finalDonorName + "\n\nFood QPP : " + finalFoodQuantity+ "\nClothes QPP : " + finalClothesQuantity+ "\nBooks QPP : " + finalBooksQuantity + "\nMedicines QPP : " +finalMedicinesQuantity + "\nAmount of Money : " + finalMoneyQuantity + "\n\nBlood Type : " + finalBloodType + "\nBlood Quantity : " + finalBloodQuantity;

        //        if(hasFoodCheck==false)
//            finalFoodQuantity="";
//        if(hasClothesCheck==false)
//            finalClothesQuantity="";
//        if(hasBooksCheck==false)
//            finalBooksQuantity="";
//        if(hasMedicinesCheck==false)
//            finalMedicinesQuantity="0";
//        if(hasMoneyCheck==false)
//            finalMoneyQuantity="";
//        if(hasBloodCheck==false)
//        {
//            finalBloodQuantity="";
//            finalBloodType="";
//        }

        //  message =   "\nName : " + finalDonorName + "\n\nFood QPP : " + finalFoodQuantity+ "\nClothes QPP : " + finalClothesQuantity+ "\nBooks QPP : " + finalBooksQuantity + "\nMedicines QPP : " +finalMedicinesQuantity + "\nAmount of Money : " + finalMoneyQuantity + "\n\nBlood Type : " + finalBloodType + "\nBlood Quantity : " + finalBloodQuantity;
        message =   "Name : " + finalDonorName;

        if(hasFoodCheck==false)
            message = message + "";
        else
            message = message + "\n\nFood : " + finalFoodQuantity;

        if(hasClothesCheck==false)
            message = message + "";
        else
            message = message +"\nClothes : " + finalClothesQuantity;

        if(hasBooksCheck==false)
            message = message + "";
        else
            message = message + "\nBooks : " + finalBooksQuantity;


        if(hasMedicinesCheck==false)
            message = message + "";
        else
            message = message + "\nMedicines : " +finalMedicinesQuantity;

        if(hasMoneyCheck==false)
            message = message + "";
        else
            message = message + "\nAmount of Money : " + finalMoneyQuantity;

        if(hasBloodCheck==false)
            message = message + "";
        else
            message = message + "\n\nBlood Type : " + finalBloodType + "\nBlood Quantity : " + finalBloodQuantity;




        try {
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailid});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Donation Details for "+ " " + orgname+ " "+ "by : " + finalDonorName);
            if (selectImageUri != null) {
                emailIntent.putExtra(Intent.EXTRA_STREAM, selectImageUri);
            }
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(emailIntent, "Sending email..."));
            upload_database();

        } catch (Throwable t) {
            Toast.makeText(this, "Request failed try again: "+ t.toString(), Toast.LENGTH_LONG).show();
        }



    }

    private  void upload_database(){
        if (flag == 1) {
            databaseReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                @Override
                public void onSuccess(ListResult listResult) {
                    imageCount = listResult.getItems().size();
                    fileName = "/Image" + String.valueOf(imageCount);
                    StorageReference databaseReference1 = firebaseStorage.getReference().child(donoremail + fileName);
                    databaseReference1.putFile(selectImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String name = taskSnapshot.getMetadata().getName();
                            final String[] url = new String[1];
                            databaseReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url[0] = uri.toString();
                                    uploadInfo = new UploadInfo(name, url[0]);
                                    Date date = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(date);
                                    boolean complete = false;
                                    String key = mDatabase_donate.child(encodedemail).push().getKey();
                                    String date_received = "";
                                    Donation_Data_Org org_data = new Donation_Data_Org(key, donoremail, finalDonorName, finalFoodQuantity, finalClothesQuantity, finalBooksQuantity, finalMedicinesQuantity, finalBloodQuantity, finalMoneyQuantity, formattedDate, date_received, complete, uploadInfo);
                                    mDatabase_donate.child(encodedemail).child(key).setValue(org_data);
                                }
                            });
                        }
                    });
                }
            });
        }
        else{
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(date);
            boolean complete = false;
            String key = mDatabase_donate.child(encodedemail).push().getKey();
            String date_received = "";
            Donation_Data_Org org_data = new Donation_Data_Org(key, donoremail, finalDonorName, finalFoodQuantity, finalClothesQuantity, finalBooksQuantity, finalMedicinesQuantity, finalBloodQuantity, finalMoneyQuantity, formattedDate, date_received, complete, uploadInfo);
            mDatabase_donate.child(encodedemail).child(key).setValue(org_data);
        }
    }
}