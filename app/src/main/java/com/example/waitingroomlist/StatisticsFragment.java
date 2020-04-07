package com.example.waitingroomlist;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

public class StatisticsFragment extends Fragment {
    View rootView;
    DatabaseHelper myDB;
    Button btnViewAll;
    TextView numOfPatientsText;
    ArrayList<Patient> patients;
    MutableLiveData<ArrayList> mutable;

    public StatisticsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
        btnViewAll=(Button) rootView.findViewById(R.id.checkList);
        numOfPatientsText=(TextView) rootView.findViewById(R.id.numberOfPatients);

        myDB = new DatabaseHelper(getActivity());
        Cursor res = myDB.getAllData();
        int numberOfPatients = res.getCount();
        numOfPatientsText.setText(Integer.toString(numberOfPatients));

//        //ArrayList<Patient> patientList = getArguments().getParcelableArrayList("fragPatient");
//        ArrayList<Patient>  patientListFrag =new ArrayList<Patient>();
//        Bundle bundle = this.getArguments();
//        if (bundle != null) {
//            patientListFrag = bundle.getParcelableArrayList("fragList");
//        }
//
//        numOfPatientsText.setText(Integer.toString(patientListFrag.size()));




        viewAll();
        return rootView;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyViewModel model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        model.getPatients().observe(this, new Observer<ArrayList<Patient>>() {
            @Override
            public void onChanged(ArrayList<Patient> patients) {
                // Update the UI.
                numOfPatientsText.setText(Integer.toString(patients.size()));
            }
        });
    }




    public void viewAll(){
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllData();
                if (res.getCount()==0){
                    //means no data is available
                    //error
                    Toast.makeText(getActivity(),"No Data", Toast.LENGTH_LONG).show();
                    return;
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
                    }

                    //Show all data
                    showMessage("Data", buffer.toString());
                }

            }
        });
    }


    public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
