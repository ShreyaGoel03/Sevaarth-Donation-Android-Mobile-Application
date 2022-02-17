package com.example.sevaarth;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView register,forgotPassword;
    private EditText editTextEmailMain,editTextPasswordMain;
    private DatabaseReference reference_user, reference_rating;
    private String userKey;
    String email, usertype;
    private int enable;
    private Button signIn;
    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            this.finishAffinity(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }


    private FirebaseAuth myAuth;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        register = (TextView) findViewById(R.id.registerTextNew);
        register.setOnClickListener(this);

        signIn = (Button) findViewById(R.id.loginButton);
        signIn.setOnClickListener(this);

        editTextEmailMain = (EditText) findViewById(R.id.emailMain);
        editTextPasswordMain = (EditText) findViewById(R.id.passwordMain);

        myAuth = FirebaseAuth.getInstance();

        forgotPassword = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerTextNew:
                startActivity(new Intent(LoginActivity.this,RegisterUser.class));
                break;
            case R.id.loginButton:
                userLogin();
                break;
            case R.id.forgotPasswordText:
                startActivity(new Intent(LoginActivity.this,ForgotPassword.class));
                break;
        }
    }
    private void userLogin() {
        String emailMain = editTextEmailMain.getText().toString().trim();
        String passwordMain = editTextPasswordMain.getText().toString().trim();

        if(emailMain.isEmpty())
        {
            editTextEmailMain.setError("Email is a required field !");
            editTextEmailMain.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(emailMain).matches())
        {
            editTextEmailMain.setError("Please provide Valid Email !");
            editTextEmailMain.requestFocus();
            return;
        }
        if(passwordMain.isEmpty())
        {
            editTextPasswordMain.setError("Password is a required field !");
            editTextPasswordMain.requestFocus();
            return;
        }
        if(passwordMain.length()<6)
        {
            editTextPasswordMain.setError("Minimum length of password should be 6 characters !");
            editTextPasswordMain.requestFocus();
            return;
        }

        myAuth.signInWithEmailAndPassword(emailMain,passwordMain).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified() ) {
                        reference_user = FirebaseDatabase.getInstance().getReference("Users");
                        String encoded_email = EncodeString(emailMain);
                        reference_user.child(encoded_email).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    usertype = dataSnapshot.child("userType").getValue(String.class);
                                    enable = dataSnapshot.child("enable").getValue(Integer.class);
                                    if(usertype.equals("DONOR")){
                                        if (enable == 1) {
                                            Intent intent = new Intent(LoginActivity.this, Donor.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            Toast.makeText(LoginActivity.this, "Account Disabled! Fraudulent Activities Detected", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        startActivity(new Intent(LoginActivity.this, DashboardOrgActivity.class));
                                    }
                                }
//
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {}
                        });

                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(LoginActivity.this,"Check your Email to verify your account",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Failed to Login ! Please check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

}