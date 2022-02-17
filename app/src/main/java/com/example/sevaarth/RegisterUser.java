package com.example.sevaarth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private TextView registerText,registerUser;
    private EditText editTextFullName,editTextEmail,editTextPassword;

    private RadioGroup radioGroup;
    public RadioButton radioButton;

    private String userType;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerText = (TextView) findViewById(R.id.back_to_sign_in);
        registerText.setOnClickListener(this);

        registerUser = (Button) findViewById(R.id.registerUser);
        registerUser.setOnClickListener(this);

        editTextFullName = (EditText) findViewById(R.id.fullName);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPassword = (EditText) findViewById(R.id.password);

        radioGroup = (RadioGroup) findViewById(R.id.groupRadio);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.back_to_sign_in:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void radioGroup() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

            // Check which radio button has been clicked
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // Get the selected Radio Button
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
            }
        });
    }



    private void registerUser() {
        String fullName = editTextFullName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        Log.d("RUN", email);
        radioButton  = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
        userType = radioButton.getText().toString().trim();

        if(fullName.isEmpty())
        {
            editTextFullName.setError("Full Name is a required field !");
            editTextFullName.requestFocus();
            return;
        }

        if(email.isEmpty())
        {
            editTextEmail.setError("Email is a required field !");
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editTextEmail.setError("Please provide Valid Email !");
            editTextEmail.requestFocus();
            return;
        }

        if(password.isEmpty())
        {
            editTextPassword.setError("Password is a required field !");
            editTextPassword.requestFocus();
            return;
        }

        if(password.length()<6)
        {
            editTextPassword.setError("Minimum length of password should be 6 characters !");
            editTextPassword.requestFocus();
            return;
        }


        Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            User user = new User(fullName,email,userType, 1);
                            String encodedemail = EncodeString(email);
                            Log.d("RUN", encodedemail);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(encodedemail)
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(RegisterUser.this,"User has been registered successfully !",Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(RegisterUser.this,"Failed to Register. User already exists !",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(RegisterUser.this,"Failed to Register. User already exists !",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }
}