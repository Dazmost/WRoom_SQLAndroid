package com.example.waitingroomlist;


import android.content.Context;
import android.graphics.drawable.GradientDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.os.CountDownTimer;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PatientAdapter extends ArrayAdapter<Patient> {

    private static final String LOCATION_SEPARATOR = " of ";

    //MULTIPLE SELECT////////////////////////////////////
    Context context;
    LayoutInflater inflater;
    List<Patient> patients;
    private SparseBooleanArray mSelectedItemsIds;
    //MULTIPLE SELECT////////////////////////////////////






    public PatientAdapter(Context context, int resourceId, ArrayList<Patient> patient) {
        super(context, resourceId, patient);

        //MULTIPLE SELECT////////////////////////////////////
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.patients = patient;
        inflater = LayoutInflater.from(context);
        //MULTIPLE SELECT////////////////////////////////////

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Patient currentPatient = getItem(position);

        //Patient line number
        final TextView numberTextView = (TextView) listItemView.findViewById(R.id.number);
        GradientDrawable magnitudeCircle = (GradientDrawable) numberTextView.getBackground();
        int magnitudeColor = R.color.magnitude1;
        magnitudeCircle.setColor(ContextCompat.getColor(getContext(), magnitudeColor));
        numberTextView.setText(Integer.toString(currentPatient.getmLineNumber()));

        //Appointment label
        TextView appointmentTextView = (TextView) listItemView.findViewById(R.id.appointment);
        appointmentTextView.setText(R.string.appointment);

        //Patient appointment time
        Date appdateObject = new Date(currentPatient.getmAppointmentTime());
        TextView appointmentTimeTextView = (TextView) listItemView.findViewById(R.id.appointment_time);
        String formattedTime = formatTime(appdateObject);
        appointmentTimeTextView.setText(formattedTime);

        //COUNTDOWN FEATURE////////////////////////////////////////////
        final TextView patientCountDown = (TextView) listItemView.findViewById(R.id.countdown);
        final View listItemViewCopy = listItemView;

        long currentTime = System.currentTimeMillis();

        //final CountDownTimer task;

        if (currentPatient.getmAppointmentTime() > currentTime) {
            long countDown = currentPatient.getmAppointmentTime() - currentTime;

                new CountDownTimer(countDown, 1000) {
                    public void onTick(long millisUntilFinished) {

                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
                        patientCountDown.setText(hms);
                    }

                    public void onFinish() {
                        patientCountDown.setText("00:00:00");
                        GradientDrawable magnitudeCircle = (GradientDrawable) numberTextView.getBackground();
                        int magnitudeColor = R.color.magnitude8;
                        magnitudeCircle.setColor(ContextCompat.getColor(listItemViewCopy.getContext(), magnitudeColor));

                    }

                    public void safeCancel(){
                        patientCountDown.setText("00:00:00");
                        super.cancel();
                    }
                }.start();

        }else{
            patientCountDown.setText("00:00:00");
            magnitudeCircle = (GradientDrawable) numberTextView.getBackground();
            magnitudeColor = R.color.magnitude8;
            magnitudeCircle.setColor(ContextCompat.getColor(listItemViewCopy.getContext(), magnitudeColor));
        }


        /////////////////////////////////////////////

        //Patient code
        TextView patientcodeTextView = (TextView) listItemView.findViewById(R.id.patientcode);
        String patientCodeString="#"+Integer.toString(currentPatient.getmPatientCode());
        patientcodeTextView.setText(patientCodeString);

        //Patient name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.Name);
        nameTextView.setText(currentPatient.getmName());

        //Check-in label
        TextView checkinTextView = (TextView) listItemView.findViewById(R.id.checkin);
        checkinTextView.setText(R.string.checkin);

        //Patient check-in date
        Date dateObject = new Date(currentPatient.getmTime());
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        dateTextView.setText(formattedDate);

        //Patient check-in time
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime2 = formatTime(dateObject);
        timeTextView.setText(formattedTime2);

        //return super.getView(position, convertView, parent);

        return listItemView;
    }



    //MULTIPLE SELECT/////////////////////////////////////////////////////////
    @Override
    public void remove(Patient object) {
        patients.remove(object);
        notifyDataSetChanged();
    }

    public List<Patient> getWorldPopulation() {
        return patients;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    //MULTIPLE SELECT/////////////////////////////////////////////////////////


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

    /**
     * //COUNTDOWN
     *     //start timer function
     *     private void startTimer(long countDown) {
     *         cTimer=new CountDownTimer(countDown, 1000) {
     *             public void onTick(long millisUntilFinished) {
     *
     *                 String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
     *                         TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % TimeUnit.HOURS.toMinutes(1),
     *                         TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % TimeUnit.MINUTES.toSeconds(1));
     *                 patientCountDown.setText(hms);
     *             }
     *             public void onFinish() {
     *                 patientCountDown.setText("00:00:00");
     *                 GradientDrawable magnitudeCircle = (GradientDrawable) numberTextView.getBackground();
     *                 int magnitudeColor = R.color.magnitude8;
     *                 magnitudeCircle.setColor(ContextCompat.getColor(listItemViewCopy.getContext(), magnitudeColor));
     *             }
     *         };
     *         cTimer.start();
     *     }
     *
     *     //cancel Timer()
     *     private void cancelTimer(){
     *         if(cTimer!=null){
     *             cTimer.cancel();
     *         }
     *     }
     *
     */



}
