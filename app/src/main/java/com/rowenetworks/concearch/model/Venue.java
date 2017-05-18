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
    private String mCity;
    private String mState;
    private String mCountry;
    private String mWebsite;
    private ArrayList<Concert> mConcerts;

    public Venue(int id, String name, String city, String state, String country, String website)   {
        mId = id;
        mName = name;
        mCity = city;
        mState = state;
        mCountry = country;
        mWebsite = website;
    }

    public String getName() {
        return mName;
    }

    public int getId()   { return mId; }

    public String getCity() { return mCity; }

    public String getState() { return mState; }

    public String getCountry()  { return mCountry; }

    public String getUrl()  { return mWebsite; }

    public ArrayList<Concert> getConcerts() { return mConcerts; }
}
