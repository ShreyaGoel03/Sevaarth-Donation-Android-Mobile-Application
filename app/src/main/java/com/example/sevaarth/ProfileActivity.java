package com.example.sevaarth;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference_user, reference_org;
    private String userKey;
    private String email,name;
    private FirebaseAuth firebaseAuth;
    private int verify_status,docs_verify,details_verify;
    private EditText name_view, email_view, password_view;
    private Button update_pass, upload_docs, upload_details;
    private TextView docs_view, verify_view, details_view;
    private int doc_status = 0, docs_update=0, details_status=0;

    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    protected void displaying_status(DatabaseReference reference, String email){
        reference.child(email).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    verify_status = snapshot.child("verify").getValue(Integer.class);
                    docs_verify = snapshot.child("docs_verify").getValue(Integer.class);
                    details_verify = snapshot.child("details_verify").getValue(Integer.class);
                    doc_status = snapshot.child("docs_status").getValue(Integer.class);
                    details_status = snapshot.child("details_status").getValue(Integer.class);
                    if(doc_status == 1) {
                        docs_view.setText("Documents has been Uploaded. Please wait for verification!");
                    }
                    else if (doc_status == 0){
                        docs_view.setText("Upload Documents to get Verified");
                    }
                    if(details_status == 1) {
                        details_view.setText("Details Filled. Please wait for verification!");
                    }
                    else if (details_status == 0){
                        details_view.setText("Please Complete the Details");
                    }
                    if (details_verify == 1){
                        details_view.setText("Details are correct. Verified!");
                    }
                    else if (details_verify == 2){
                        details_view.setText("Details are missing. Mail at abc@iiitd.ac.in");
                    }
                    if (docs_verify == 1){
                        docs_view.setText("Documents are correct. Verified!");
                    }
                    else if (docs_verify == 2){
                        docs_view.setText("Some Documents are missing. Mail at abc@iiitd.ac.in");
                    }
                    if(details_verify == 1 && docs_verify == 1) {
                        verify_status = 1;
                        Org_Details det = snapshot.getValue(Org_Details.class);
                        det.setVerify(verify_status);
                        reference_org.child(userKey).setValue(det);
                        verify_view.setText("You have been Verified! :)");
                    }
                    else{
                        verify_view.setText("You are not Verified! Please Wait");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something wrong happened !",Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_org);
        name_view = (EditText)findViewById(R.id.namep_edit);
        email_view = (EditText)findViewById(R.id.emailp_edit);
        password_view = (EditText)findViewById(R.id.passwordp_edit);
        update_pass = (Button)findViewById(R.id.change_pass);
        upload_details = (Button)findViewById(R.id.details_profile_btn);
        upload_docs = (Button)findViewById(R.id.docs_profile_btn);
        docs_view = (TextView)findViewById(R.id.docs_verify);
        details_view = (TextView)findViewById(R.id.det_verify);
        verify_view = (TextView)findViewById(R.id.verify_value);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        reference_user = FirebaseDatabase.getInstance().getReference("Users");
        reference_org = FirebaseDatabase.getInstance().getReference("Organisation");
        userKey = user.getEmail();
        userKey = EncodeString(userKey);
        reference_user.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                email = dataSnapshot.child("email").getValue(String.class);
                email_view.setText(email);
                email = EncodeString(email);
                name = dataSnapshot.child("fullName").getValue(String.class);
                name_view.setText(name);
                update_pass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showChangePasswordDialog();
                    }
                });
                upload_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(ProfileActivity.this, DetailsActivity.class));
                    }
                });
                upload_docs.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent typeIntent = new Intent(v.getContext(),UploadActivity.class);
                        int type = 2;
                        typeIntent.putExtra("Type", type);
                        startActivityForResult(typeIntent, 0);
                    }
                });
                displaying_status(reference_org,email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void showChangePasswordDialog() {
        View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.update_password, null);
        final EditText passwordold = view.findViewById(R.id.passwordEt);
        final EditText passwordnew = view.findViewById(R.id.newpasswordEt);
        Button updatepass = view.findViewById(R.id.updatePasswordBtn);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.show();

        updatepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldpass = passwordold.getText().toString().trim();
                String newpass = passwordnew.getText().toString().trim();
                if (TextUtils.isEmpty(oldpass)){
                    Toast.makeText(ProfileActivity.this, "Enter Your Current Password....", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpass.length()<6){
                    Toast.makeText(ProfileActivity.this, "Password Length must have atleast 6 characters....", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                updatePassword(oldpass,newpass);
            }
        });
    }

    private void updatePassword(String oldpass, String newpass) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), oldpass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newpass)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ProfileActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0 && resultCode==RESULT_OK){
            if(data.getIntExtra("RES",0) == 1){
                docs_update = 1;
                Log.d("RUN", "value " );
                user = FirebaseAuth.getInstance().getCurrentUser();
                firebaseAuth = FirebaseAuth.getInstance();
                reference_org = FirebaseDatabase.getInstance().getReference("Organisation");
                userKey = user.getEmail();
                userKey = EncodeString(userKey);
                reference_org.child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Org_Details det = dataSnapshot.getValue(Org_Details.class);
                        det.setDocs_status(docs_update);
                        reference_org.child(userKey).setValue(det);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
            }

        }
    }


}
