package com.example.sevaarth;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class DisplayEvents extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String finalDonationType;
    private TextView textViewDate;
    private EditText selectDate,selectTime,selectDescription,selectState,selectCity;
    private TimePickerDialog timePickerDialog;
    private DatePickerDialog.OnDateSetListener setListener;
    private Calendar calendarTime;
    private int currentHour,currentMinute;
    private String amPm;
    private String finalState,finalCity,finalDate,finalTime,finalDescription;
    private String message;
    String emailid;
    String encodedemail;
    String userencodedmail;
    private FirebaseAuth mAuth;
    private DatabaseReference reference_event;
    FirebaseUser user;
    private DatabaseReference mDatabase;
    public static String EncodeString(String string) {
        return string.replace(".", ",");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_events);

        Spinner typeSpinner = findViewById(R.id.donationTypes);
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(this, R.array.donationType, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setOnItemSelectedListener(this);

        selectDate = findViewById(R.id.donationDateEdit);
        selectTime = findViewById(R.id.donationTimeEdit);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);


        user = FirebaseAuth.getInstance().getCurrentUser();
        String userkey = user.getEmail();
        userencodedmail=EncodeString(userkey);


//        emailid = getIntent().getStringExtra("Email id");
//        encodedemail=EncodeString(emailid);
        mAuth = FirebaseAuth.getInstance();
        //FirebaseDatabase.getInstance().getReference().child("Events").setValue(true);

        reference_event = FirebaseDatabase.getInstance().getReference("Events");





        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DisplayEvents.this, android.R.style.Theme_Holo_Dialog_MinWidth, setListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                finalDate = day + "/" + month + "/" + year;
                selectDate.setText(finalDate);
            }
        };

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(DisplayEvents.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month + 1;
                        finalDate = day + "/" + month + "/" + year;
                        selectDate.setText(finalDate);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarTime = Calendar.getInstance();
                currentHour = calendarTime.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendarTime.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(DisplayEvents.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if(hourOfDay >= 12)
                            amPm = "PM";
                        else
                            amPm = "AM";
                        finalTime = String.format("%02d:%02d",hourOfDay,minute)+" " + amPm;
                        selectTime.setText(finalTime);
                    }
                },currentHour,currentMinute,false);
                timePickerDialog.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        if(text.equals("Food") || text.equals("Clothes") || text.equals("Books") || text.equals("Medicines") || text.equals("Money") || text.equals("Blood") || text.equals("Multiple"))
        {
            finalDonationType = text;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void hostEvent(View view) {


        selectDescription = findViewById(R.id.donationDescriptionEdit);
        selectState = findViewById(R.id.donationStateEdit);
        selectCity = findViewById(R.id.donationCityEdit);

        finalState = selectState.getText().toString();
        finalCity = selectCity.getText().toString();
        finalDescription = selectDescription.getText().toString();





//        Calendar calendarEvent = Calendar.getInstance();
//        Intent i = new Intent(Intent.ACTION_EDIT);
//        i.setType("vnd.android.cursor.item/event");
//        i.putExtra("beginTime", calendarEvent.getTimeInMillis());
//        i.putExtra("allDay", true);
//        i.putExtra("rule", "FREQ=YEARLY");
//        i.putExtra("endTime", calendarEvent.getTimeInMillis() + 60 * 60 * 1000);
//        i.putExtra("title", "Calendar Event");
//        startActivity(i);

        message = "Type of donation : " +finalDonationType+ "\n\nState : " +finalState+ "\n\nCity : " +finalCity+ "\n\nDate : " +finalDate+ "\n\nTime : " +finalTime+ "\n\nDescription : " +finalDescription;



        Events events1=new Events();
        events1.setEvent_Details(message);
        events1.setDate(finalDate);
        events1.setTime(finalTime);
        events1.setCity(finalCity);
        events1.setState(finalState);
        events1.setType(finalDonationType);
        reference_event.child(userencodedmail).setValue(events1);

//        reference_event.child(userencodedmail).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                //  Toast.makeText(DisplayDetails.this,snapshot.child("address").getValue(String.class),Toast.LENGTH_LONG).show();
//                // snapshot.getValue(Org_Details.class);
//                //Toast.makeText(DisplayDetails.this,details.area,Toast.LENGTH_LONG).show();
//
//                String message1=snapshot.child("events_Details").getValue().toString();
//
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//
//
//        });





        Intent intent= new Intent(this, ShowHostEvents.class);
        intent.putExtra("MESSAGE",message);
        startActivity(intent);

    }
}