package com.example.waitingroomlist;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    View rootView;
    DatabaseHelper myDB;
    Button btnViewAll;
    TextView numOfPatientsText;
    ArrayList<Patient> patients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        btnViewAll=(Button) findViewById(R.id.checkList);
        numOfPatientsText=(TextView) findViewById(R.id.numberOfPatients);

        myDB = new DatabaseHelper(this);
        Cursor res = myDB.getAllData();
        int numberOfPatients = res.getCount();
        numOfPatientsText.setText(Integer.toString(numberOfPatients));
        viewAll();
    }

    public void viewAll(){
        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDB.getAllData();
                if (res.getCount()==0){
                    //means no data is available
                    //error
                    Toast.makeText(getApplicationContext(),"No Data", Toast.LENGTH_LONG).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
