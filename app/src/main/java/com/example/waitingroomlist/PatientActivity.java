package com.example.waitingroomlist;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PatientActivity extends AppCompatActivity {

    ArrayList<Patient> patientList;
    EditText name;
    TimePicker appointmentTimeWidget;
    TextView patientCodeTextView;
    Button submit;
    long epochTime;
    long currentTimeInMilli=1;
    long appointmentTimeTotal=0;
    Boolean timeCheck=false;
    Boolean randomNumberCheck=true;
    int randomPatientCode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        Intent intent = getIntent();
        patientList=new ArrayList<>();
        patientList= intent.getParcelableArrayListExtra("patientList");

        randomPatientCode = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);

        if (!patientList.isEmpty()) {
            while (randomNumberCheck) {
                for (int i = 0; i < patientList.size(); i++) {
                    Patient currentPatient = patientList.get(i);
                    if (randomPatientCode == currentPatient.getmPatientCode()) {
                        randomPatientCode = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
                        randomNumberCheck = true;
                        break;
                    } else {
                        randomNumberCheck = false;
                    }

                }
            }
        }

        patientCodeTextView=(TextView)findViewById(R.id.randomPatientCode);
        String patientCodeString="Patient Code: #"+Integer.toString(randomPatientCode);
        patientCodeTextView.setText(patientCodeString);


        submit=(Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name=(EditText)findViewById(R.id.name_submit);
                String nameString = name.getText().toString();

                    appointmentTimeWidget = (TimePicker) findViewById(R.id.timePicker);
                    int timeHour = appointmentTimeWidget.getHour();
                    int timeMinute = appointmentTimeWidget.getMinute();

                    appointmentTimeTotal=convertTimePickerToAppointmentTIme(timeHour,timeMinute);

                    Date today = Calendar.getInstance().getTime();
                    currentTimeInMilli = today.getTime();
                    if(appointmentTimeTotal<currentTimeInMilli){
                        Toast.makeText(PatientActivity.this, "Invalid Time", Toast.LENGTH_LONG).show();
                    }else{
                        timeCheck=true;
                    }

                    if (timeCheck) {
                        Patient currentPatient = new Patient(randomPatientCode, nameString, patientList.size()+1, currentTimeInMilli, appointmentTimeTotal);
                        patientList.add(currentPatient);

                        Collections.sort(patientList);

                        //Edit LineNumber to reflect new order
                        for (int j = 0; j < patientList.size(); j++) {
                            Patient lineNumberPatient = patientList.get(j);
                            lineNumberPatient.setmLineNumber(j + 1);
                            patientList.set(j, lineNumberPatient);
                        }

                        //New position of patient after sorting
                        int newPosition=0;
                        for (int i= 0; i<patientList.size(); i++){
                            Patient p= patientList.get(i);
                            if (currentPatient.getmPatientCode()==p.getmPatientCode()){
                                newPosition=i;
                            }
                        }

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("patientList", patientList);
                        returnIntent.putExtra("patientPosition",newPosition);
                        returnIntent.putExtra("button",true);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
            }
        });


    }

    public long convertTimePickerToAppointmentTIme (int timeHour, int timeMinute) {
        long timeHourToMilli = TimeUnit.MINUTES.toMillis(timeMinute);
        long timeMinuteToMilli = TimeUnit.HOURS.toMillis(timeHour);
        long TimeTotal = timeHourToMilli + timeMinuteToMilli;

        Date today = Calendar.getInstance().getTime();


        // Constructs a SimpleDateFormat using the given pattern
        SimpleDateFormat crunchifyFormat = new SimpleDateFormat("LLL dd, yyyy");
        // format() formats a Date into a date/time string.
        String currentTime = crunchifyFormat.format(today);
        try {
            // parse() parses text from the beginning of the given string to produce a date.
            Date date = crunchifyFormat.parse(currentTime);
            // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
            epochTime = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        TimeTotal += epochTime;
        return TimeTotal;
    }
}
