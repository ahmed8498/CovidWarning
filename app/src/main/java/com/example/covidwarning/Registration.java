package com.example.covidwarning;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covidwarning.Callbacks.RegisterUserCallback;
import com.example.covidwarning.Callbacks.UploadingMediaCallback;
import com.example.covidwarning.Models.User;
import com.example.covidwarning.Network.FirebaseNetworking;

import java.io.IOException;
import java.util.ArrayList;

public class Registration extends AppCompatActivity implements RegisterUserCallback, UploadingMediaCallback {



    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button selectPhotosButton;
    private Button registerButton;
    private TextView photosSelectionTextView;
    private ProgressBar progressBar;
    private User newUser;
    Uri selectedVideoUri;

    private ArrayList<Bitmap> imagesList;

     final static int  GALLERY_REQUEST = 1;
     final static int VIDEO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        nameEditText = (EditText) findViewById(R.id.registrationNameEditText);
        emailEditText =  (EditText) findViewById(R.id.registrationEmailEditText);
        passwordEditText =  (EditText) findViewById(R.id.registrationPasswordEditText);
        confirmPasswordEditText =  (EditText) findViewById(R.id.registrationConfirmPasswordEditText);
        selectPhotosButton =  (Button)findViewById(R.id.registrationPhotosButton);
        registerButton =  (Button) findViewById(R.id.registerButton);
        photosSelectionTextView = (TextView) findViewById(R.id.photosSelectedTextView);
        progressBar = (ProgressBar) findViewById(R.id.registrationProgressBar);
        imagesList = new ArrayList<Bitmap>();


    }


    public void createAccountButtonClicked(View v)
    {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(registerButton.getWindowToken(), 0);
        if(nameEditText.getText().toString().equals("") )
           nameEditText.setError("Please enter your name");

        else if (emailEditText.getText().toString().equals("") || !emailEditText.getText().toString().contains("aus.edu") || emailEditText.getText().toString().trim().length() != 17 )
        {
            emailEditText.setError("Please enter a valid AUS email");
        }
        else if(passwordEditText.getText().toString().equals(""))
        {
            passwordEditText.setError("Please enter your password");
        }
        else if(confirmPasswordEditText.getText().toString().equals(""))
        {
            confirmPasswordEditText.setError("Please confirm your password");
        }
        else if(!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())){
            passwordEditText.setError("Passwords do not match");
        }
        else if(imagesList.size() == 0 && selectedVideoUri == null){
            Toast.makeText(this,"Please choose identification media",Toast.LENGTH_LONG).show();

        }
        else if (imagesList.size() < 5 && imagesList.size() > 0)
        {
            Toast.makeText(this,"Please choose at least 5 images",Toast.LENGTH_LONG).show();
        }

        else{

            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();
            String name = nameEditText.getText().toString();
            String id = email.substring(4,9);
            newUser = new  User(email,name,id);
            FirebaseNetworking firebaseNetworking = new FirebaseNetworking();
            if(imagesList.size() >= 5)
            {
                progressBar.setVisibility(View.VISIBLE);
                firebaseNetworking.registerUser(newUser,password,this,this);

            }
            else if(selectedVideoUri != null){
                progressBar.setVisibility(View.VISIBLE);
                firebaseNetworking.registerUser(newUser,password,this,this);

            }
            else{
                Toast.makeText(this,"Please choose a media file",Toast.LENGTH_LONG).show();
            }
        }

    }

    public void selectPhotosButtonClicked(View v)
    {
        imagesList = new ArrayList<Bitmap>();
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        photoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
    }

    public void selectVideoButtonClicked(View V)
    {
        Intent videoPickerIntent = new Intent(Intent.ACTION_PICK);
        videoPickerIntent.setType("video/*");
        videoPickerIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(videoPickerIntent, VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch(requestCode)
            {
                case GALLERY_REQUEST:
                if(data.getClipData() != null) {

                    int count = data.getClipData().getItemCount();

                        for (int i = 0; i < count; i++) {
                            Uri selectedImage = data.getClipData().getItemAt(i).getUri();
                            try {
                                if (Build.VERSION.SDK_INT >= 29) {
                                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);
                                    Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                                    Log.d("LOGCAT", "onActivityResult: " + bitmap);
                                    imagesList.add(bitmap);
                                } else {
                                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                                    imagesList.add(bitmap);
                                }
                            } catch (IOException e) {

                            }
                        }

                }
                else if(data.getData() != null)
                {
                    Uri selectedImage = data.getData();
                    try {
                        if (Build.VERSION.SDK_INT >= 29) {
                            ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), selectedImage);
                            Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                            Log.d("LOGCAT", "onActivityResult: " + bitmap);
                            imagesList.add(bitmap);
                        } else {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            imagesList.add(bitmap);
                        }
                    } catch (IOException e) {

                    }
                }
                break;
                case VIDEO_REQUEST:
                    if(data.getData() != null) {


                        selectedVideoUri = data.getData();
                        }




                    }

            }
        }


    @Override
    public void onCallback() {

        FirebaseNetworking firebaseNetworking = new FirebaseNetworking();
        if(imagesList.size() > 0) {
            firebaseNetworking.uploadImagesToFirebase(imagesList, newUser,this);
        }
        else if (selectedVideoUri != null)
        {
            firebaseNetworking.uploadVideo(selectedVideoUri,newUser,this);
        }
    }

    @Override
    public void onMediaCallback(Boolean isSuccessful) {
            if (isSuccessful){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Account created successfully!",Toast.LENGTH_LONG).show();
                finish();
            }
            else
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Account creation Failed!",Toast.LENGTH_LONG).show();
            }
    }
}
