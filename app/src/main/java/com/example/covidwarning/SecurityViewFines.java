package com.example.covidwarning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.covidwarning.Callbacks.GetFinesCallback;
import com.example.covidwarning.Models.Adapters.ViewFineRecyclerViewAdapter;
import com.example.covidwarning.Models.Fine;
import com.example.covidwarning.Network.FirebaseNetworking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class SecurityViewFines extends AppCompatActivity implements GetFinesCallback {


    private EditText dateEditText;
    private Spinner fineTypeSpinner;
    private RecyclerView recyclerView;
    private TextView totalCount;
    private Button viewFinesButton;
    final Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_view_fines);

        dateEditText = (EditText) findViewById(R.id.viewAllFinesDateEditText);
        fineTypeSpinner = (Spinner) findViewById(R.id.viewAllFinesTypeSpinner);
        recyclerView = (RecyclerView) findViewById(R.id.viewAllFinesRecyclerView);
        totalCount = (TextView) findViewById(R.id.viewAllFinesTotalCount);
        viewFinesButton = (Button)findViewById(R.id.viewAllFinesButton);
        setupDateEditText();


    }

    public void viewAllFinesButtonClicked(View view)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(viewFinesButton.getWindowToken(), 0);
        if(dateEditText.getText().toString().isEmpty()){
            dateEditText.setError("Please select a date");
        }
        else{

            String date = dateEditText.getText().toString();
            String type = fineTypeSpinner.getSelectedItem().toString();
            FirebaseNetworking firebaseNetworking = new FirebaseNetworking();
            firebaseNetworking.getFines(date,type,this);
        }

    }
    @Override
    public void onBackPressed() {
        String[] options = {"Add Fine","View Social Distance Violations","Log Out","Close App"};

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
                        SecurityViewFines.super.onBackPressed();

                }
            }
        });
        builder.show();
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
                new DatePickerDialog(SecurityViewFines.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
    public void onCallback(Boolean isSuccessful, ArrayList<Fine> fines) {
        ViewFineRecyclerViewAdapter adapter = new ViewFineRecyclerViewAdapter(fines);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        if(fines.size()> 0)
            totalCount.setText("Total number: " + fines.size());
        else
            totalCount.setText("Total number: 0" );


    }
}
