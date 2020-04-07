package com.example.waitingroomlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ModifyPatientActivity extends AppCompatActivity {

    ArrayList<Patient> patientList;
    Patient currentPatient;
    int patientPosition = 0;
    TimePicker appointmentTimeWidget;
    TextView patientCode;
    TextView patientName;
    TextView patientPositionText;
    TextView patientCheckInDate;
    TextView patientCheckInTime;
    TextView patientAppointment;
    TextView patientCountDown;
    Button submit;
    Button cancel;

    long epochTime;
    long currentTimeInMilli = 1;
    long appointmentTimeTotal = 0;
    Boolean timeCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modifypatient);

        //Receive Intent from MainActivity
        Intent intent = getIntent();
        patientList = new ArrayList<>();
        patientList = intent.getParcelableArrayListExtra("patientList");
        patientPosition = intent.getIntExtra("patientPosition", 0);
        currentPatient = patientList.get(patientPosition);

        //Patient code display
        patientCode = (TextView) findViewById(R.id.patientcodeModify);
        String patientCodeString="#"+Integer.toString(currentPatient.getmPatientCode());
        patientCode.setText(patientCodeString);

        //Name display
        patientName = (TextView) findViewById(R.id.nameModify);
        patientName.setText(currentPatient.getmName());

        //Position display
        patientPositionText=(TextView)findViewById(R.id.positionModify) ;
        patientPositionText.setText(Integer.toString(currentPatient.getmLineNumber()));

        //Check-in date display
        Date dateObject = new Date(currentPatient.getmTime());
        patientCheckInDate = (TextView) findViewById(R.id.dateModify);
        String formattedDate = formatDate(dateObject);
        patientCheckInDate.setText(formattedDate);

        //Check-in time display
        patientCheckInTime = (TextView) findViewById(R.id.timeModify);
        String formattedTime2 = formatTime(dateObject);
        patientCheckInTime.setText(formattedTime2);

        //Appointment time display
        Date appdateObject = new Date(currentPatient.getmAppointmentTime());
        patientAppointment = (TextView) findViewById(R.id.appointment_timeModify);
        String formattedTime = formatTime(appdateObject);
        patientAppointment.setText(formattedTime);


        //COUNTDOWN FEATURE////////////////////////////////////////////
        patientCountDown = (TextView) findViewById(R.id.countdown_Modify);

            long currentTime = System.currentTimeMillis();
            long countDown = currentPatient.getmAppointmentTime()-currentTime;

            new CountDownTimer(countDown, 1000) {
                public void onTick(long millisUntilFinished) {

                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
                    patientCountDown.setText(hms);
                }
                public void onFinish() {
                    patientCountDown.setText("00:00:00");
                }
            }.start();

        /////////////////////////////////////////////


        //Cancel button click
        cancel = (Button) findViewById(R.id.cancel_modify);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("patientList", patientList);
                returnIntent.putExtra("button",false);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        //Submit button click
        submit = (Button) findViewById(R.id.submit_modify);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appointmentTimeWidget = (TimePicker) findViewById(R.id.timePicker_modify);
                int timeHour = appointmentTimeWidget.getHour();
                int timeMinute = appointmentTimeWidget.getMinute();
                long timeHourToMilli = TimeUnit.MINUTES.toMillis(timeMinute);
                long timeMinuteToMilli = TimeUnit.HOURS.toMillis(timeHour);
                appointmentTimeTotal = timeHourToMilli + timeMinuteToMilli;

                Date today = Calendar.getInstance().getTime();
                currentTimeInMilli = today.getTime();

                // Constructs a SimpleDateFormat using the given pattern
                SimpleDateFormat crunchifyFormat = new SimpleDateFormat("LLL dd, yyyy");
//                // format() formats a Date into a date/time string.
                String currentTime = crunchifyFormat.format(today);
                try {
                    // parse() parses text from the beginning of the given string to produce a date.
                    Date date = crunchifyFormat.parse(currentTime);
                    // getTime() returns the number of milliseconds since January 1, 1970, 00:00:00 GMT represented by this Date object.
                    epochTime = date.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                appointmentTimeTotal += epochTime;

                if (appointmentTimeTotal < currentTimeInMilli) {
                    Toast.makeText(ModifyPatientActivity.this, "Invalid Time", Toast.LENGTH_LONG).show();
                } else {
                    timeCheck = true;
                }

                if (timeCheck) {
                    Log.d("Halim", Integer.toString(patientPosition));

                    Patient newPatientTime = new Patient(currentPatient.getmPatientCode(), currentPatient.getmName(), patientPosition + 1, currentPatient.getmTime(), appointmentTimeTotal);
                    patientList.set(patientPosition,newPatientTime);

                    //Sort the ArrayList of Patients by appointment times
                    Collections.sort(patientList);

                    for(Patient p:patientList){
                        System.out.println(p);
                    }

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

                    for (int a = 0; a < patientList.size(); a++) {
                        Patient test = patientList.get(a);
                        Log.d("Halim RESULT ON APP", test.getmName());
                    }

                    timeCheck = false;

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

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}

