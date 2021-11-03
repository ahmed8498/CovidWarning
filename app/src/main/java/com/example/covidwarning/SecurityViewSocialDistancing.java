package com.example.covidwarning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidwarning.Callbacks.GetFinesCallback;
import com.example.covidwarning.Models.Adapters.ViewFineIntervalReyclerViewAdapter;
import com.example.covidwarning.Models.Fine;
import com.example.covidwarning.Models.FineInterval;
import com.example.covidwarning.Network.FirebaseNetworking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SecurityViewSocialDistancing extends AppCompatActivity implements GetFinesCallback {

    private EditText dateEditText;
    private Spinner locationsSpinner;
    final Calendar myCalendar = Calendar.getInstance();
    private TextView totalCountLabel;
    private RecyclerView recyclerView;
    private FineInterval[] fineIntervalsArray = new FineInterval[9];
    private int[] fineIntervalCount = new int[10]; //Each item represents one interval and the last one represents any time outside 8-5


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_view_social_distancing);
        dateEditText = (EditText) findViewById(R.id.viewFineDateEditText);
        locationsSpinner = (Spinner) findViewById(R.id.viewFineLocationSpinner);
        recyclerView = (RecyclerView) findViewById(R.id.securityFineRecyclerView);
        totalCountLabel = (TextView) findViewById(R.id.totalFinesCountLabel);
        setupDateEditText();

        for(int i = 0 ; i < fineIntervalCount.length;i++)
            fineIntervalCount[0] = 0;
    }


    public void securityViewFinesButtonClicked(View view)
    {
        if(dateEditText.getText().toString().isEmpty()){
            dateEditText.setError("Please select a date");
        }
        else{

            String date = dateEditText.getText().toString();
            String location = locationsSpinner.getSelectedItem().toString();
            FirebaseNetworking firebaseNetworking = new FirebaseNetworking();
            firebaseNetworking.getFineByDateAndLocation(date,location,this);
        }

    }

    private void setupDateEditText(){

        dateEditText.setOnClickListener(new View.OnClickListener() {

            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateEditText();

                }

            };
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SecurityViewSocialDistancing.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateEditText()
    {
        String dateFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        dateEditText.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onBackPressed() {
        String[] options = {"Add Fine","View Fines","Log Out","Close App"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What do you want to do?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which){
                    case 0:
                        Intent i = new Intent(getApplicationContext(), AddFine.class);
                        startActivity(i);
                        finish();
                        break;
                    case 1:
                        Intent ii = new Intent(getApplicationContext(), SecurityViewFines.class);
                        startActivity(ii);
                        finish();
                        break;
                    case 2:
                        Intent j = new Intent(getApplicationContext(), Login.class);
                        startActivity(j);
                        finish();
                        break;
                    case 3:
                        SecurityViewSocialDistancing.super.onBackPressed();

                }
            }
        });
        builder.show();
    }

    @Override
    public void onCallback(Boolean isSuccessful, ArrayList<Fine> fines) {
        if(isSuccessful){

            for(Fine f:fines)
            {
               String timeString = f.getFineTime();
               String hour = timeString.substring(0,2);
               if(hour.equals("08")){
                   fineIntervalCount[0]++ ;
               }
               else if(hour.equals("09")) {
                   fineIntervalCount[1]++;
            }
               else if(hour.equals("10")) {
                   fineIntervalCount[2]++;
               }
               else if(hour.equals("11")) {
                   fineIntervalCount[3]++;
               }
               else if(hour.equals("12")) {
                   fineIntervalCount[4]++;
               }
               else if(hour.equals("13")) {
                   fineIntervalCount[5]++;
               }
               else if(hour.equals("14")) {
                   fineIntervalCount[6]++;
               }
               else if(hour.equals("15")) {
                   fineIntervalCount[7]++;
               }
               else if(hour.equals("16")) {
                   fineIntervalCount[8]++;
               }
               else{
                   fineIntervalCount[9]++;
               }
            }

            totalCountLabel.setText("Total number of Violations: " + fines.size());

            ViewFineIntervalReyclerViewAdapter adapter = new ViewFineIntervalReyclerViewAdapter(fineIntervalCount);
            recyclerView.setAdapter(adapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));


        }
        else{
            Toast.makeText(getApplicationContext(),"No Violations found", Toast.LENGTH_LONG).show();
        };



    }
}
