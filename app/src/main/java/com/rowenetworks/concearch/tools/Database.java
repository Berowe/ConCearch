package com.rowenetworks.concearch.tools;

import android.util.SparseArray;

import com.rowenetworks.concearch.model.Artist;
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.model.Venue;

/**
 * @author Braxton Rowe
 * @version 1.0
 * This class is a tool that stores and retrieves Artists, Venues, and Concerts.
 */

public class Database {

    private static SparseArray<Artist> artistArray;
    private static SparseArray<Concert> concertArray;
    private static SparseArray<Venue> venueArray;

    public static void initializeDatabase() {
        artistArray = new SparseArray<>();
        concertArray = new SparseArray<>();
        venueArray = new SparseArray<>();
    }

    public static void addArtist(int key, Artist artist)    {
        artistArray.put(key, artist);
    }

    public static void addConcert(int key, Concert concert)    {
        concertArray.put(key, concert);
    }

    public static void addVenue(int key, Venue venue)    {
        venueArray.put(key, venue);
    }

    public static Artist getArtist(int key)   {
        return artistArray.get(key);
    }

    public static Concert getConcert(int key)   {
        return concertArray.get(key);
    }

    public static Venue getVenue(int key)   {
        return venueArray.get(key);
    }

}
