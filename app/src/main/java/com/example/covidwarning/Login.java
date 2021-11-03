package com.example.covidwarning;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.covidwarning.Callbacks.LoginCallback;
import com.example.covidwarning.Network.FirebaseNetworking;

public class Login extends AppCompatActivity implements LoginCallback {

    private EditText emailEditText;
    private EditText passwordEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
    }


    public void loginButtonClicked(View v)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(passwordEditText.getWindowToken(), 0);
        if(emailEditText.getText().toString().isEmpty())
        {
            emailEditText.setError("Please enter your email");
        }
        else if(passwordEditText.getText().toString().isEmpty())
        {
            passwordEditText.setError("Please enter your password");
        }
        else{
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            FirebaseNetworking firebaseNetworking = new FirebaseNetworking();
            firebaseNetworking.loginUser(email,password,this,this);
        }
    }

    public void createAccountButtonClicked(View v)
    {
        Intent i = new Intent(this,Registration.class);
        startActivity(i);
    }

    @Override
    public void onCallback(Boolean isSuccessful, Exception e,String email) {
        if(isSuccessful){
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LoggedInUser",email);
            editor.commit();
            if(email.toLowerCase().contains("security")){
                String[] options = {"Add New Fine","View Social Distancing Violations","View Fines"};

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
                                Intent j = new Intent(getApplicationContext(), SecurityViewSocialDistancing.class);
                                startActivity(j);
                                finish();
                                break;
                            case 2:
                                Intent k = new Intent(getApplicationContext(), SecurityViewFines.class);
                                startActivity(k);
                                finish();
                                break;
                        }
                    }
                });
                builder.show();
            }
            else{
                Intent i = new Intent(getApplicationContext(), StudentViewFines.class);
                startActivity(i);
                finish();

            }


        }
        else
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
