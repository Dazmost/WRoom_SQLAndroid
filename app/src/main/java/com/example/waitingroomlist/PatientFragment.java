package com.example.waitingroomlist;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.AbsListView.MultiChoiceModeListener;

import java.util.ArrayList;

public class PatientFragment extends Fragment {

    View rootView;
    DatabaseHelper myDB;
    private MyViewModel model;
    PatientAdapter adapter;
    ListView patientListView;


    static final int PATIENT_CONTACT_REQUEST = 1; // The request code.
    static final int MODIFY_PATIENT_REQUEST = 2; // The request code.

    private FloatingActionButton fab;
    public ArrayList<Patient> patients;

    public PatientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_patient, container, false);

        patients = new ArrayList<Patient>();
        //patients.add(new Patient(1234, "Steve", 1, 1000, 10000));
        //patients.add(new Patient(4321, "Bob Macquerie", 1, 1000000, 1000000));

        //SQL management
        myDB = new DatabaseHelper(getActivity());
        patients=getAll(patients);
        //^SQL management


        //ListItem Click
        //updateUi(patients); //Update the UI and onclicklistener
        adapter = new PatientAdapter(getActivity(),R.layout.list_item,patients);
        patientListView = (ListView) rootView.findViewById(R.id.list);
        //patientListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Set the adapter onto the view pager
        patientListView.setAdapter(adapter);


        //MULTIPLE SELECT/////////////////////////////////////
        patientListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        patientListView.setMultiChoiceModeListener(new MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode,
                                                  int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = patientListView.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete:
                        // Calls getSelectedIds method from ListViewAdapter Class
                        SparseBooleanArray selected = adapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Patient selecteditem = adapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                adapter.remove(selecteditem);

                                ///////////////////////////////////ADDED////////////////////
                                patients.remove(selecteditem);
                                DeleteData(selecteditem.getmPatientCode());
                                ///////////////////////////////////^ADDED////////////////////
                            }
                        }
                        ///////////////////////////////////ADDED////////////////////
                        //New position of patient after sorting
                        for (int i= 0; i<patients.size(); i++){
                            Patient p= patients.get(i);
                            p.setmLineNumber(i+1);
                            patients.set(i,p);
                        }

                        //Edit LineNumber to reflect new order in SQL
                        for (int j = 0; j < patients.size(); j++) {
                            Patient lineNumberPatient = patients.get(j);
                            UpdateData(lineNumberPatient.getmPatientCode(),lineNumberPatient.getmName(),
                                    lineNumberPatient.getmLineNumber(), lineNumberPatient.getmTime(),
                                    lineNumberPatient.getmAppointmentTime());
                        }

                        ///////////////////////////////////^ADDED////////////////////

                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.fragment_patient, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                adapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });
        //MULTIPLE SELECT/////////////////////////////////////

        patientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient patient = patients.get(position);
                Log.d("Halim Item", patients.toString()+ "ArraySize: " + Integer.toString(patients.size()) + " Position:" + (Integer.toString(position)));
                System.out.println(patients.toString());

                Intent intentPatientModify = new Intent(getActivity(),ModifyPatientActivity.class);
                intentPatientModify.putExtra("patientList", patients);
                intentPatientModify.putExtra("patientPosition",position);

                //Fragment patient ArrayList to send
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("fragList", patients);
                // set Fragmentclass Arguments
                Fragment fragobj = new StatisticsFragment();
                fragobj.setArguments(bundle);
                //^Fragment patient ArrayList to send

                startActivityForResult(intentPatientModify,MODIFY_PATIENT_REQUEST);
            }
        });



        //Floating Action Button Click
        fab= (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Fab Clicked", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getActivity(),PatientActivity.class);
                intent.putExtra("patientList", patients);
                startActivityForResult(intent,PATIENT_CONTACT_REQUEST);

            }
        });

        return rootView;
    }

    //Runs after onCreateView
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        model.setPatients(patients);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        // Check which request we're responding to
        if (requestCode == PATIENT_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                patients=data.getParcelableArrayListExtra("patientList");
                final int newPatientPosition=data.getIntExtra("patientPosition", 0);
                final boolean buttonPress = data.getBooleanExtra("button",false);

                //SQL method
                Patient currentPatient=patients.get(newPatientPosition);
                AddData(currentPatient.getmPatientCode(),currentPatient.getmName(),currentPatient.getmLineNumber(),
                        currentPatient.getmTime(),currentPatient.getmAppointmentTime());

                //ViewModel Update
                model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
                model.setPatients(patients);


                //Edit LineNumber to reflect new order in SQL
                for (int j = 0; j < patients.size(); j++) {
                    Patient lineNumberPatient = patients.get(j);
                    UpdateData(lineNumberPatient.getmPatientCode(),lineNumberPatient.getmName(),
                            lineNumberPatient.getmLineNumber(), lineNumberPatient.getmTime(),
                            lineNumberPatient.getmAppointmentTime());
                }


                Log.d("Halim", patients.toString()+ " " + Integer.toString(patients.size()));
                System.out.println(patients.toString());

                final ListView patientListView = (ListView) rootView.findViewById(R.id.list);
                // Do something with the contact here (bigger example below)
                adapter = new PatientAdapter(getActivity(),R.layout.list_item,patients);
                //patientListView = (ListView) findViewById(R.id.list);
                // Set the adapter onto the view pager
                patientListView.setAdapter(adapter);
                patientListView.setSelection(newPatientPosition);

                if (buttonPress) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateView(newPatientPosition, patientListView);
                        }
                    }, 10);
                }


            }
        }

        // Check which request we're responding to
        if (requestCode == MODIFY_PATIENT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.


                patients=data.getParcelableArrayListExtra("patientList");
                final int newPatientPosition=data.getIntExtra("patientPosition", 0);
                final boolean buttonPress = data.getBooleanExtra("button",false);

                //SQL method
                Patient currentPatient=patients.get(newPatientPosition);
                UpdateData(currentPatient.getmPatientCode(),currentPatient.getmName(),currentPatient.getmLineNumber(),
                currentPatient.getmTime(),currentPatient.getmAppointmentTime());

                //Edit LineNumber to reflect new order in SQL
                for (int j = 0; j < patients.size(); j++) {
                    Patient lineNumberPatient = patients.get(j);
                    UpdateData(lineNumberPatient.getmPatientCode(),lineNumberPatient.getmName(),
                            lineNumberPatient.getmLineNumber(), lineNumberPatient.getmTime(),
                            lineNumberPatient.getmAppointmentTime());
                }


                adapter = new PatientAdapter(getActivity(),R.layout.list_item,patients);
                final ListView patientListView = (ListView) rootView.findViewById(R.id.list);
                // Set the adapter onto the view pager
                patientListView.setAdapter(adapter);
                //patientListView.smoothScrollToPositionFromTop(newPatientPosition);
                patientListView.setSelection(newPatientPosition);

                if (buttonPress) {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updateView(newPatientPosition, patientListView);
                        }
                    }, 10);
                }


            }
        }
    }


    private void updateView(int index, ListView patientListView){
        adapter = new PatientAdapter(getActivity(),R.layout.list_item,patients);
        final View v = patientListView.getChildAt(index - patientListView.getFirstVisiblePosition());

        if(v == null)
            return;

        final TextView numberTextView = (TextView) v.findViewById(R.id.number);
        GradientDrawable magnitudeCircle = (GradientDrawable) numberTextView.getBackground();
        int magnitudeColor = R.color.green;
        magnitudeCircle.setColor(ContextCompat.getColor(v.getContext(),magnitudeColor));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GradientDrawable magnitudeCircle = (GradientDrawable) numberTextView.getBackground();
                int magnitudeColor = R.color.magnitude1;
                magnitudeCircle.setColor(ContextCompat.getColor(v.getContext(),magnitudeColor));
            }
        }, 2000);

    }



    //SQL METHODS
    public void DeleteData(int mPatientCode){
                Integer deletedRows = myDB.deleteData(Integer.toString(mPatientCode));
                if (deletedRows>0){
                    Toast.makeText(getActivity(),"Data Deleted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "Data not Deleted", Toast.LENGTH_SHORT).show();
                }
    }

    public void UpdateData(int mPatientCode, String mName, int mLineNumber, long mTime, long mAppointmentTime){
                boolean isUpdate = myDB.updateData(Integer.toString(mPatientCode),
                        mName, Integer.toString(mLineNumber), Long.toString(mTime), Long.toString(mAppointmentTime));
                if (isUpdate==true){
                    Toast.makeText(getActivity(),"Data Updated", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(),"Data not Updated", Toast.LENGTH_SHORT).show();
                }
    }

    public void AddData(int mPatientCode, String mName, int mLineNumber, long mTime, long mAppointmentTime){

        boolean isInserted = myDB.insertData(Integer.toString(mPatientCode),
                mName, Integer.toString(mLineNumber), Long.toString(mTime), Long.toString(mAppointmentTime));

        if (isInserted==true){
            Toast.makeText(getActivity(),"Data Inserted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getActivity(),"Data not Inserted", Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Patient> getAll(ArrayList<Patient> patientList){
        Cursor res = myDB.getAllData();
        if (res.getCount()==0){
            //means no data is available
            //error
            Toast.makeText(getActivity(),"No Data", Toast.LENGTH_SHORT).show();
            return new ArrayList<Patient>();
        }else{

            StringBuffer buffer = new StringBuffer();
            //get all data one by one through res object
            while(res.moveToNext()){
                buffer.append("Code : "+res.getString(0)+"\n");
                buffer.append("Name : " +res.getString(1)+"\n");
                buffer.append("Line Number : "+res.getString(2)+"\n");
                buffer.append("Time : "+res.getString(3)+"\n");
                buffer.append("Appointment : "+res.getString(4)+"\n\n");

                int code=Integer.parseInt(res.getString(0));
                String name=res.getString(1);
                int linenumber=Integer.parseInt(res.getString(2));
                long time =Long.parseLong(res.getString(3));
                long appointment = Long.parseLong(res.getString(4));

                patientList.add(new Patient(code,name,linenumber,time,appointment));

            }
        }
        return patientList;
    }
    //^SQL METHODS


}

