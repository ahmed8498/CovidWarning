package com.example.covidwarning.Network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.covidwarning.Callbacks.AddingFineCallback;
import com.example.covidwarning.Callbacks.GetFinesCallback;
import com.example.covidwarning.Callbacks.LoginCallback;
import com.example.covidwarning.Callbacks.RegisterUserCallback;
import com.example.covidwarning.Callbacks.UploadingMediaCallback;
import com.example.covidwarning.Models.Fine;
import com.example.covidwarning.Models.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executor;

public class FirebaseNetworking {

    private StorageReference mStorageRef;
    private ArrayList<Bitmap> imagesList;
    private ArrayList<Uri> downloadUriList = new ArrayList<Uri>();
    private FirebaseAuth mAuth;
    private RegisterUserCallback registerUserCallback;
    private LoginCallback loginCallback;
    private AddingFineCallback addingFineCallback;
    private GetFinesCallback getFinesCallback;
    private UploadingMediaCallback uploadingMediaCallback;


    public void uploadImagesToFirebase(ArrayList<Bitmap> imagesList,User user,UploadingMediaCallback callback){
        this.imagesList = imagesList;
        uploadingMediaCallback = callback;

        uploadImages(user);
    }
    public void uploadImages(final User user)
    {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        if(imagesList.size() > 0)
        {
            final StorageReference ref = mStorageRef.child(user.getAusID() + "-" +String.valueOf(System.currentTimeMillis()));
            Bitmap bitmap = imagesList.get(0);
            imagesList.remove(0);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = ref.putBytes(data);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task< Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        uploadImages(user);
                    }
                    else
                    {
                            Log.d("TAG", "onComplete: Upload task failed");
                    }
                }
            });
        }
        else {
            uploadingMediaCallback.onMediaCallback(true);
        }
    }

    public void uploadVideo(Uri videoUri,User user,UploadingMediaCallback callback)
    {
        this.uploadingMediaCallback = callback;
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference ref = mStorageRef.child(user.getAusID() + "-" +String.valueOf(System.currentTimeMillis()));
        UploadTask uploadTask = ref.putFile(videoUri);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    System.out.println("Succesful video");
                    uploadingMediaCallback.onMediaCallback(true);
                }
                else
                {
                    System.out.println("Failed video");
                    uploadingMediaCallback.onMediaCallback(false);
                }
            }
        });
    }


    public void registerUser(final User user, String password,  Activity activity,RegisterUserCallback registerUserCallback) {
        mAuth = FirebaseAuth.getInstance();
        this.registerUserCallback = registerUserCallback;
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            addUser(user);

                        } else {
                            // If sign in fails, display a message to the user.
                           try{
                            throw task.getException();}
                            catch(FirebaseAuthUserCollisionException e)
                            {
                                Toast.makeText(activity,"User already exists",Toast.LENGTH_LONG).show();
                            }
                           catch(FirebaseAuthWeakPasswordException e) {
                               Toast.makeText(activity,"Weak password. Please enter another password",Toast.LENGTH_LONG).show();

                           }
                            catch(Exception e){

                            }

                            Log.w("TAG", "createUserWithEmail:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    public void addUser(User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> userMap = user.toMap();

        db.collection("users")
                .add(userMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        registerUserCallback.onCallback();

                    }
                });
    }

    public void loginUser(String email,String password,Activity activity,LoginCallback callback){
        this.loginCallback = callback;
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            loginCallback.onCallback(true,null,email);

                        } else {
                            loginCallback.onCallback(false,task.getException(),email);
                        }

                        // ...
                    }
                });
    }

    public void addFine(Fine fine,AddingFineCallback callback)
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        this.addingFineCallback = callback;
        Map<String, Object> fineMap = fine.toMap();

        db.collection("fines")
                .add(fineMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        addingFineCallback.onCallback(true,null);

                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                addingFineCallback.onCallback(false,e);
            }
        });
    }

    public void getFineByDateAndLocation(String date,String location,GetFinesCallback callback){
        this.getFinesCallback = callback;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("fines")
                .whereEqualTo("fineDate", date)
                .whereEqualTo("fineLocation",location)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Fine> fineArrayList = new ArrayList<Fine>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String fineID = (String)document.get("fineID");
                                String fineDate = (String)document.get("fineDate");
                                String fineLocation = (String)document.get("fineLocation");
                                String fineTime = (String)document.get("fineTime");
                                String fineAmount = (String) document.get("fineAmount");
                                String fineType = (String)document.get("fineType");
                                String studentID = (String) document.get("studentID");
                                fineArrayList.add(new Fine(fineID,fineLocation,fineDate,fineTime,fineAmount,fineType,studentID));
                            }
                            getFinesCallback.onCallback(true,fineArrayList);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            getFinesCallback.onCallback(false,null);
                        }
                    }
                });
    }


    public void getSocialDistancingViolationOfStudent(String studentID, GetFinesCallback callback){
        this.getFinesCallback = callback;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("fines")
                .whereEqualTo("studentID",studentID)
                .whereEqualTo("fineType","Not maintaining social distance")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Fine> fineArrayList = new ArrayList<Fine>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                String fineID = (String)document.get("fineID");
                                String fineDate = (String)document.get("fineDate");
                                String fineLocation = (String)document.get("fineLocation");
                                String fineTime = (String)document.get("fineTime");
                                String fineAmount = (String) document.get("fineAmount");
                                String fineType = (String)document.get("fineType");
                                String studentID = (String)document.get("studentID");
                                fineArrayList.add(new Fine(fineID,fineLocation,fineDate,fineTime,fineAmount,fineType,studentID));
                            }
                            getFinesCallback.onCallback(true,fineArrayList);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                            getFinesCallback.onCallback(false,null);
                        }
                    }
                });
    }

    public void getFines(String date,String fineType,GetFinesCallback callback){
        this.getFinesCallback = callback;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if(fineType.equals("All")) {
            db.collection("fines")
                    .whereEqualTo("fineDate", date)
                    .whereNotEqualTo("fineType","Not maintaining social distance")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Fine> fineArrayList = new ArrayList<Fine>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    String fineID = (String) document.get("fineID");
                                    String fineDate = (String) document.get("fineDate");
                                    String fineLocation = (String) document.get("fineLocation");
                                    String fineTime = (String) document.get("fineTime");
                                    String fineAmount = (String) document.get("fineAmount");
                                    String fineType = (String) document.get("fineType");
                                    String studentID = (String) document.get("studentID");
                                    fineArrayList.add(new Fine(fineID, fineLocation, fineDate, fineTime, fineAmount, fineType, studentID));
                                }
                                getFinesCallback.onCallback(true, fineArrayList);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                getFinesCallback.onCallback(false, null);
                            }
                        }
                    });
        }
        else{
            db.collection("fines")
                    .whereEqualTo("fineDate", date)
                    .whereEqualTo("fineType", fineType)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<Fine> fineArrayList = new ArrayList<Fine>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("TAG", document.getId() + " => " + document.getData());
                                    String fineID = (String) document.get("fineID");
                                    String fineDate = (String) document.get("fineDate");
                                    String fineLocation = (String) document.get("fineLocation");
                                    String fineTime = (String) document.get("fineTime");
                                    String fineAmount = (String) document.get("fineAmount");
                                    String fineType = (String) document.get("fineType");
                                    String studentID = (String) document.get("studentID");
                                    fineArrayList.add(new Fine(fineID, fineLocation, fineDate, fineTime, fineAmount, fineType, studentID));
                                }
                                getFinesCallback.onCallback(true, fineArrayList);
                            } else {
                                Log.d("TAG", "Error getting documents: ", task.getException());
                                getFinesCallback.onCallback(false, null);
                            }
                        }
                    });
        }
    }
}
