package com.example.sevaarth;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class UploadActivity extends AppCompatActivity {
    private Button browse, upload;
    private ImageView imageView;
    Uri filepath;
    Bitmap bmap;
    DatabaseReference mDatabase;
    private FirebaseUser user;
    private DatabaseReference mDataBase_user;
    FirebaseStorage firebaseStorage;
    StorageReference databaseReference;
    private String userKey,email, fileName="";
    Intent resultintent,typeIntent;
    private int imageCount=0, filetype=0, docsCount=0,imgdone=0,docdone=0, flag=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        browse = findViewById(R.id.browse_btn);
        upload = findViewById(R.id.upload_btn);
        imageView =  findViewById(R.id.image_view);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Organisation");
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDataBase_user = FirebaseDatabase.getInstance().getReference();
        email = EncodeString(user.getEmail());
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = firebaseStorage.getReference().child(email);
        resultintent = new Intent();
        typeIntent = getIntent();
        if(typeIntent == null){
            Log.d("NULL","Intent not received");
        }
        else{
            Log.d("NULL","Intent received");
        }
        filetype = typeIntent.getIntExtra("Type",0);
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(UploadActivity.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent.createChooser(intent,"Please pick an Image to Upload"),1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();


            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
    }

    private void uploadImage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Image");
        progressDialog.show();

        databaseReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                imageCount = 0;
                docsCount = 0;
                Log.d("RUN", "Flag1 " + String.valueOf(flag));
                for(int i = 0; i < listResult.getItems().size(); i++) {
                    StorageReference item = listResult.getItems().get(i);
                    String name = item.getName();
                    if (name.startsWith("Ima")) {
                        imageCount += 1;
                    } else {
                        docsCount += 1;
                    }
                }
                if (filetype == 1) {
                    fileName = "/Image" + String.valueOf(imageCount);
                    flag = 1;
                } else if (filetype == 2) {
                    fileName = "/Doc" + String.valueOf(docsCount);
                }
                StorageReference databaseReference1 = firebaseStorage.getReference().child(email + fileName);
                databaseReference1.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            String name = taskSnapshot.getMetadata().getName();
                            final String[] url = new String[1];
                            databaseReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    url[0] = uri.toString();
                                    if (name.startsWith("Ima")) {
                                        UploadInfo uploadInfo = new UploadInfo(name, url[0]);
                                        mDatabase.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    mDatabase.child(email).child("images").push().setValue(uploadInfo);
                                                    Log.d("RUN", "Snapshot" + snapshot);
                                                    Log.d("RUN", String.valueOf(snapshot.child("images").getValue(UploadInfo.class)));
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            });
                        Log.d("RUN", "Flag3 " + String.valueOf(flag));
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "File Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        if (filetype == 1) {
                            if (imageCount > 0) {
                                imgdone = 1;
                            }
                            resultintent.putExtra("RES", imgdone);
                            setResult(RESULT_OK, resultintent);
                        } else {
                            if (docsCount > 0) {
                                docdone = 1;
                            }
                            resultintent.putExtra("RES", docdone);
                            setResult(RESULT_OK, resultintent);
                        }
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        float percent_done = (snapshot.getBytesTransferred() * 100) / snapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + (int) percent_done + " %");
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream ir = getContentResolver().openInputStream(filepath);
                bmap = BitmapFactory.decodeStream(ir);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageBitmap(bmap);
            } catch (Exception e) {

            }
        }
    }

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}
