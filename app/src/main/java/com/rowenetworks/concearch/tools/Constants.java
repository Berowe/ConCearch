package com.rowenetworks.concearch.tools;

/**
 * @author Braxton Rowe
 * @version 1.0
 * A list of String constants for use within the application.
 */

public class Constants {

    public static final String LIST_KEY = "list";
    public static final String ARTIST_OBJECT_KEY = "artist_object";
    public static final String CONCERT_OBJECT_KEY = "event_object";
    public static final String VENUE_OBJECT_KEY = "venue_object";

    public static final String SONGKICK_ARTIST_SEARCH_URL_BEGINNING =
            "http://api.songkick.com/api/3.0/search/artists.json?query=";
    public static final String SONGKICK_SELECTED_ARTIST_URL_BEGINNING =
            "http://api.songkick.com/api/3.0/artists/";
    public static final String SONGKICK_SELECTED_ARTIST_URL_END =
            "/calendar.json?apikey=io09K9l3ebJxmxe2";
    public static final String LASTFM_URL_BEGINNING =
            "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=";
    public static final String LASTFM_URL_END = "" +
            "&api_key=43d8e0220d48794ceade532ae023c947&format=json";
    public static final String SONGKICK_VENUE_SEARCH_URL_BEGINNING =
            "http://api.songkick.com/api/3.0/search/venues.json?query=";
    public static final String SONGKICK_VENUE_DETAILS_SEARCH_URL_BEGINNING =
            "http://api.songkick.com/api/3.0/venues/";
    public static final String SONGKICK_VENUE_DETAILS_SEARCH_URL_END =
            "/calendar.json?apikey=w6Xcjutcc9YOPSeQ";
    public static final String SONGKICK_API_KEY = "&apikey=w6Xcjutcc9YOPSeQ";
    //Here for possible future functionality
    public static final String SONGKICK_LOCATION_SEARCH_URL_BEGINNING =
            "http://api.songkick.com/api/3.0/events.json?location=geo:";

    public static final String NO_IMAGE = "No Image Available";
    public static final String NO_BIO = "No Biography Available";

}