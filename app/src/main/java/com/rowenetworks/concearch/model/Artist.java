package com.rowenetworks.concearch.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * This class is the model for Artists.
 */

public class Artist implements Serializable {

    private String mName;
    private int mId;
    private String mImageUrl;
    //Here for possible future functionality
    private String mSongKickUrl;
    private Boolean mOnTour;
    private ArrayList<String> mSimilarArtists;
    private String mBiography;
    private ArrayList<Concert> mConcerts;

    private Boolean mLastFm;

    public Artist(int id, String name, String songKickUrl, Boolean onTour)  {
        mOnTour = onTour;
        mName = name;
        mSongKickUrl = songKickUrl;
        mId = id;
        mLastFm = false;
    }

    public String getName() {
        return mName;
    }

    public int getId()  {
        return mId;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public Boolean getOnTour()  {
        return mOnTour;
    }

    public ArrayList<String> getSimilarArtists()    {
        return mSimilarArtists;
    }

    public String getBiography() { return mBiography; }

    public ArrayList<Concert> getConcerts() { return mConcerts; }

    public Boolean checkLastFm() { return mLastFm; }

    public void setConcerts(ArrayList<Concert> concerts) { mConcerts = concerts; }

    public void setImage(String url) { mImageUrl = url; }

    public void setBiography(String bio) { mBiography = bio; }

    public void setSimArtists(ArrayList<String> artists) {
        mSimilarArtists = new ArrayList<>();
        mSimilarArtists = artists;
    }

    public void setLastFM(Boolean set) { mLastFm = set; }
}
