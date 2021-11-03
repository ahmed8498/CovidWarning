package com.example.covidwarning.Callbacks;

import com.example.covidwarning.Models.Fine;

import java.util.ArrayList;

public interface GetFinesCallback {

    public void  onCallback(Boolean isSuccessful,ArrayList<Fine> fines);
}
