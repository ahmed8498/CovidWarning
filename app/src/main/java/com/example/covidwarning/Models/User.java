package com.example.covidwarning.Models;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String email;
    private String name;
    private String ausID;

    public User(String email, String name, String ausID) {
        this.email = email;
        this.name = name;
        this.ausID = ausID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAusID() {
        return ausID;
    }

    public void setAusID(String ausID) {
        this.ausID = ausID;
    }

    public Map<String,Object> toMap(){

        Map<String, Object> user = new HashMap<>();
        user.put("name",name);
        user.put("email",email);
        user.put("id",ausID);
        return user;
    }
}
