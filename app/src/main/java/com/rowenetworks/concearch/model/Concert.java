package com.rowenetworks.concearch.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * This class is the model for Events.
 */

public class Concert implements Serializable  {

    private String mName;
    private int mId;
    //Here for possible future functionality.
    private String mCity;
    private String mWebsite;
    private Venue mVenue;
    private ArrayList<Artist> mArtists;


    public Concert(int id, String name, String city, String website, Venue venue,
                   ArrayList<Artist> artists)   {
        mId = id;
        mName = name;
        mCity = city;
        mWebsite = website;
        mVenue = venue;
        mArtists = artists;
    }

    public int getId()  {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getCity() {return mCity; }

    public String getWebsite()  {
        return mWebsite;
    }

    public Venue getVenue() {
        return mVenue;
    }

    public ArrayList<Artist> getArtists()   {
        return mArtists;
    }

}
