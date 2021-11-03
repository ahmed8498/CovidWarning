package com.example.covidwarning.Models;

import java.util.HashMap;
import java.util.Map;

public class Fine {

    private String fineID;
    private String fineLocation;
    private String fineDate;
    private String fineTime;
    private String fineAmount;
    private String fineType;
    private String studentID;

    public Fine(String fineID, String fineLocation, String fineDate, String fineTime, String fineAmount,String fineType,String studentID) {
        this.fineID = fineID;
        this.fineLocation = fineLocation;
        this.fineDate = fineDate;
        this.fineTime = fineTime;
        this.fineAmount = fineAmount;
        this.fineType = fineType;
        this.studentID = studentID;
    }

    public String getFineID() {
        return fineID;
    }

    public void setFineID(String fineID) {
        this.fineID = fineID;
    }

    public String getFineLocation() {
        return fineLocation;
    }

    public void setFineLocation(String fineLocation) {
        this.fineLocation = fineLocation;
    }

    public String getFineDate() {
        return fineDate;
    }

    public void setFineDate(String fineDate) {
        this.fineDate = fineDate;
    }

    public String getFineTime() {
        return fineTime;
    }

    public void setFineTime(String fineTime) {
        this.fineTime = fineTime;
    }

    public String getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(String fineAmount) {
        this.fineAmount = fineAmount;
    }

    public String getFineType() {
        return fineType;
    }

    public void setFineType(String fineType) {
        this.fineType = fineType;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public Map<String, Object> toMap(){
        Map<String, Object> fine = new HashMap<>();
        fine.put("fineID",fineID);
        fine.put("fineLocation",fineLocation);
        fine.put("fineDate",fineDate);
        fine.put("fineTime",fineTime);
        fine.put("fineAmount",fineAmount);
        fine.put("fineType",fineType);
        fine.put("studentID",studentID);

        return fine;
    }
}
