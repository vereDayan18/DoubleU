package com.example.finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;


public class Request implements Parcelable {
    private String name;
    private String domain;
    private String phoneNumber;
    private String key;


    public Request() {

    }

    public Request(String name, String domain, String phoneNumber, String key) {
        this.name = name;
        this.domain = domain;
        this.phoneNumber = phoneNumber;
        this.key = key;
    }

    protected Request(Parcel in) {
        key = in.readString();
        name = in.readString();
        domain = in.readString();
        phoneNumber = in.readString();
    }

    public static final Creator<Request> CREATOR = new Creator<Request>() {
        @Override
        public Request createFromParcel(Parcel in) {
            return new Request(in);
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.getKey());
        parcel.writeString(this.getName());
        parcel.writeString(this.getDomain());
        parcel.writeString(this.getPhoneNumber());

    }

    public String getKey() {
        return key;
    }

    public String getDomain() {
        return domain;
    }


    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String toString(){
        return "Key:" + this.key + ", Name: " + this.name + ", Domain: " + this.domain + ", Phone number: " + this.phoneNumber;
    }

    public boolean compareTo(Request request) {
        if (this.getKey().equals(request.getKey())) {
            return true;
        } return false;
    }

    // Returns a Json object that contains the request details
    public static JSONObject toJsonObject(Request request){
        JSONObject JsonRequest = new JSONObject();
        try {
            JsonRequest.put("name", request.getName());
            JsonRequest.put("domain", request.getDomain());
            JsonRequest.put("phone number", request.getPhoneNumber());
        } catch (JSONException e) {
            System.err.println("Problem occurred while trying to make a Json object from Request object");
            e.printStackTrace();
        }
        return JsonRequest;
    }

}
