package com.example.covidwarning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.covidwarning.Callbacks.AddingFineCallback;
import com.example.covidwarning.Models.Fine;
import com.example.covidwarning.Network.FirebaseNetworking;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFine extends AppCompatActivity implements AddingFineCallback {


    private EditText studentIDEditText;
    private Spinner fineTypeSpinner;
    private Spinner fineLocationSpinner;
    private EditText fineDateEditText;
    private EditText fineTimeEditText;
    private EditText fineAmountEditText;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fine);

        studentIDEditText = (EditText) findViewById(R.id.fineStudentIDEditText);
        fineTypeSpinner = (Spinner) findViewById(R.id.fineTypeSpinner);
        fineLocationSpinner = (Spinner) findViewById(R.id.fineLocationSpinner);
        fineDateEditText = (EditText) findViewById(R.id.dateEditText);
        fineTimeEditText = (EditText) findViewById(R.id.timeEditText);
        fineAmountEditText = (EditText) findViewById(R.id.fineAmountEditText);
        fineAmountEditText.setEnabled(false);

        setupDateEditText();
        setupTimeEditText();
        setupFineTypeSpinner();
    }

    private void setupDateEditText(){

        fineDateEditText.setOnClickListener(new View.OnClickListener() {

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
                new DatePickerDialog(AddFine.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateDateEditText()
    {
        String dateFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        fineDateEditText.setText(sdf.format(myCalendar.getTime()));
    }


    private void setupTimeEditText(){
        fineTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar currentTimeCalendar = Calendar.getInstance();
                int hour = currentTimeCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = currentTimeCalendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddFine.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        String format = "%1$02d"; // two digits
                        String time = String.format(format,i) + ":" + String.format(format,i1);

                        fineTimeEditText.setText(time);

                    }
                },hour,minute,true);

                timePickerDialog.show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        String[] options = {"View Fines","View Social Distance Violations","Log Out","Close App"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What do you want to do?");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // the user clicked on colors[which]
                switch(which){
                    case 0:
                        Intent i = new Intent(getApplicationContext(), SecurityViewFines.class);
                        startActivity(i);
                        finish();
                        break;
                    case 1:
                        Intent ii = new Intent(getApplicationContext(), SecurityViewSocialDistancing.class);
                        startActivity(ii);
                        finish();
                        break;
                    case 2:
                        Intent j = new Intent(getApplicationContext(), Login.class);
                        startActivity(j);
                        finish();
                        break;
                    case 3:
                        AddFine.super.onBackPressed();

                }
            }
        });
        builder.show();
    }

    private void setupFineTypeSpinner(){
        fineTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String fineTypes[] = getResources().getStringArray(R.array.fineTypeArray);
                if(fineTypes[i].equals("Not wearing a mask"))
                {
                    fineAmountEditText.setText("AED 100");
                }
                else if (fineTypes[i].equals("Not maintaining social distance"))
                {
                    fineAmountEditText.setText("AED 150");
                }
                else if (fineTypes[i].equals("High body temperature"))
                {
                    fineAmountEditText.setText("0 - PCR required");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void addFineButtonClicked(View view){
        if(studentIDEditText.getText().toString().isEmpty()){
            studentIDEditText.setError("Please enter a student ID");
        }
        else if(fineDateEditText.getText().toString().isEmpty())
        {
            fineDateEditText.setError("Please enter a date");
        }
        else if(fineTimeEditText.getText().toString().isEmpty())
        {
            fineTimeEditText.setError("Please enter a time");
        }
        else{
            String studentID = studentIDEditText.getText().toString().trim();
            String fineDate = fineDateEditText.getText().toString().trim();
            String fineTime = fineTimeEditText.getText().toString().trim();
            String fineLocation = fineLocationSpinner.getSelectedItem().toString();
            String fineType = fineTypeSpinner.getSelectedItem().toString();
            String fineAmount = fineAmountEditText.getText().toString();


            Fine newFine = new Fine(studentID,fineLocation,fineDate,fineTime,fineAmount,fineType,studentID);
            FirebaseNetworking firebaseNetworking = new FirebaseNetworking();
            firebaseNetworking.addFine(newFine,this);
        }
    }

    @Override
    public void onCallback(Boolean isSuccessful, Exception e) {
        if(isSuccessful){
            Toast.makeText(getApplicationContext(),"Fine added successfully",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();

        }
    }
}
