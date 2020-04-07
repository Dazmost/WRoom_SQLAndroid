package com.example.waitingroomlist;

import android.database.Cursor;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MyViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Patient>> patients;


    public void setPatients (ArrayList<Patient> patientArray){
        if (patients == null) {
            patients = new MutableLiveData<ArrayList<Patient>>();
        }
        patients.setValue(patientArray);
    }

    public LiveData<ArrayList<Patient>> getPatients() {
        if (patients == null) {
            patients = new MutableLiveData<ArrayList<Patient>>();
            //loadUsers();
        }
        return patients;
    }

//    public void loadUsers() {
//        // Do an asynchronous operation to fetch users.
//    }

}
