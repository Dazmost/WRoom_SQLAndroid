package com.example.waitingroomlist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Patient implements Parcelable,Comparable<Patient>{

    private int mPatientCode;
    private String mName;
    private int mLineNumber;
    private long mTime;
    private long mAppointmentTime;


    public Patient (int patientCode, String name, int lineNumber, long time, long appointmentTime){
        mPatientCode=patientCode;
        mName=name;
        mLineNumber=lineNumber;
        mTime=time;
        mAppointmentTime=appointmentTime;
    }

    protected Patient(Parcel in) {
        mPatientCode = in.readInt();
        mName = in.readString();
        mLineNumber = in.readInt();
        mTime = in.readLong();
        mAppointmentTime = in.readLong();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

    public int getmPatientCode() {
        return mPatientCode;
    }

    public String getmName() {
        return mName;
    }

    public int getmLineNumber() {
        return mLineNumber;
    }

    public long getmTime() {
        return mTime;
    }

    public long getmAppointmentTime() {
        return mAppointmentTime;
    }

    public void setmAppointmentTime(long appointmentTime){
        mAppointmentTime=appointmentTime;
    }

    public void setmLineNumber(int lineNumber){
        mLineNumber=lineNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPatientCode);
        dest.writeString(mName);
        dest.writeInt(mLineNumber);
        dest.writeLong(mTime);
        dest.writeLong(mAppointmentTime);
    }

    @Override
    public int compareTo(Patient o) {
        //sorting in acending order
        return Long.compare(mAppointmentTime,o.mAppointmentTime);
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + mName + " Appointment: " + mAppointmentTime;
     }
}
