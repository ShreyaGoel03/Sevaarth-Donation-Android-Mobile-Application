package com.example.sevaarth;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class ShowHostEvents extends AppCompatActivity {

    Bitmap bmp;
    Button save;

    private TextView detailsView;
    private String message;
    private Button generatepdf;

    private FirebaseAuth mAuth;
    private DatabaseReference reference_event;
    FirebaseUser user;
    String message1;
    private DatabaseReference mDatabase;
    String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_host_events);



        Intent intent = getIntent();
        message = intent.getStringExtra("MESSAGE");
        detailsView = findViewById(R.id.showDetails);
        detailsView.setText(message);
        user = FirebaseAuth.getInstance().getCurrentUser();
        useremail = user.getEmail();
        Log.d("email",useremail);
        message= "User Emailid: " + useremail +"\n \n \n" + message;
        Log.d("email",message);

        generatepdf=findViewById(R.id.generatepdf);

        // bmp = BitmapFactory.decodeResource(getResources(),R.drawable.dmultiple);



        generatepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                {
                    if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                    {
                        String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permission,100);
                    }
                    else
                    {
                        savePdf();
                    }
                }
                else
                {
                    savePdf();
                }
            }
        });
    }

    private void savePdf() {
        Document doc = new Document();
        String myFile = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis());
        String path = Environment.getExternalStorageDirectory()+"/"+myFile+".pdf";
        Font smallbold = new Font(Font.FontFamily.TIMES_ROMAN,24,Font.BOLD);
        try {
            PdfWriter.getInstance(doc,new FileOutputStream(path));
            doc.open();
            //   String text = "This is my first pdf ! ";
//                doc.addAuthor("Anubhav");
            doc.addAuthor(useremail);
            doc.add(new Paragraph(message,smallbold));
            doc.close();
            Toast.makeText(this,""+myFile+".pdf is saved to "+path,Toast.LENGTH_LONG).show();
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Some error occured"+e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode)
        {
            case 1000:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    savePdf();
                }
                else
                {
                    Toast.makeText(this,"Permission denied ! ",Toast.LENGTH_LONG).show();
                }
        }
    }
}