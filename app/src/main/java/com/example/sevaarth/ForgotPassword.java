package com.example.sevaarth;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditTextPassword;

    private Button resetPasswordButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditTextPassword = (EditText) findViewById(R.id.editTextEmail);
        resetPasswordButton = (Button) findViewById(R.id.resetButton);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }

            private void resetPassword() {
                String emailPassword = emailEditTextPassword.getText().toString().trim();

                if(emailPassword.isEmpty())
                {
                    emailEditTextPassword.setError("Email is a required field !");
                    emailEditTextPassword.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(emailPassword).matches())
                {
                    emailEditTextPassword.setError("Please provide Valid Email !");
                    emailEditTextPassword.requestFocus();
                    return;
                }

                auth.sendPasswordResetEmail(emailPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPassword.this,"Check your email to reset the password",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(ForgotPassword.this,"Try Again !",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}