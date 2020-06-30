package com.example.finalproject;

public class VolunteerUsers {

    private String ID;
    private String name;
    private String phoneNumber;
  

    public VolunteerUsers(){
    }

    public VolunteerUsers(String ID, String name, String phoneNumber){
        this.ID = ID;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getID(){ return ID; }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setID(String ID){ this.ID = ID; }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

