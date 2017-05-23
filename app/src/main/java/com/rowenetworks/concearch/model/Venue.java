package com.rowenetworks.concearch.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * This class is the object to represent Venues.
 */

public class Venue implements Serializable  {

    private String mName;
    private int mId;
    private String mAddress;
    private String mPhone;
    private String mWebsite;
    private ArrayList<Concert> mConcerts;

    public Venue(int id, String name, String website)   {
        mId = id;
        mName = name;
        mWebsite = website;
    }

    public Venue(int id, String name, String address, String phone, String website)   {
        mId = id;
        mName = name;
        mAddress = address;
        mPhone = phone;
        mWebsite = website;
    }

    public String getName() {
        return mName;
    }

    public int getId()   { return mId; }

    public String getAddress()  { return mAddress; }

    public String getUrl()  { return mWebsite; }

    public String getPhone()    { return mPhone; }

    public void setConcerts(ArrayList<Concert> concerts) {
        mConcerts = concerts;
    }

    public ArrayList<Concert> getConcerts() { return mConcerts; }
}