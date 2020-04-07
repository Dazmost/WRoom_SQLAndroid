package com.example.waitingroomlist;

import java.util.ArrayList;

public class PatientContainer {

    private static ArrayList <Patient> mPatientList;

    /**
     * Create a private constructor because no one should ever create a {@link PatientContainer} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name PatientContainer (and an object instance of PatientContainer is not needed).
     */
    public PatientContainer(){
    }

    /**
     * max = (a > b) ? a : b;
     *
     * (a > b) ? a : b; is an expression which returns one of two values, a or b.
     * The condition, (a > b), is tested. If it is true the first value, a, is returned.
     * If it is false, the second value, b, is returned. Whichever value is returned
     * is dependent on the conditional test, a > b. The condition can be any expression
     * which returns a boolean value.
     */
    public static ArrayList<Patient> getPatientList(){
        return mPatientList==null ? new ArrayList() : mPatientList;
    }

    public static void setPatientList(ArrayList<Patient> patientList){
        mPatientList=patientList;
    }
}
