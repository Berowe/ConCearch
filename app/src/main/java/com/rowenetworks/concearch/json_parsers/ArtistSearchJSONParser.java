package com.rowenetworks.concearch.json_parsers;

import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.model.Artist;
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * Takes in a JSON object and parses it for information on artists.
 */

public class ArtistSearchJSONParser {

    private static int[] ids;
    private static ArrayList<Integer> artists;

    public static int[] artistSearch(JSONObject songKick) throws JSONException {

        artists = new ArrayList<>();
        JSONObject resultsPage = songKick.getJSONObject("resultsPage");

        if (resultsPage.getInt("totalEntries") > 0) {
            JSONObject results = resultsPage.getJSONObject("results");
            JSONArray artists = results.getJSONArray("artist");
            parseArray(artists);
            makeIds();
        } else {
            ids = new int[0];
        }

        return ids;
    }

    private static void parseArray(JSONArray artists) throws JSONException  {

        for (int i = 0; i < artists.length(); i++)  {
            JSONObject artist = artists.getJSONObject(i);
            parseArtist(artist);
        }
    }

    private static void parseArtist(JSONObject artist) throws JSONException {
        Boolean onTour = false;
        String name;
        String songKickUrl;
        int id;

        if (artist.has("onTourUntil"))   {
            if (!artist.getString("onTourUntil").equals("null")) {
                onTour = true;
            }
        }

        name = artist.getString("displayName");
        songKickUrl = artist.getString("uri");

        if (onTour) {
            id = artist.getInt("id");
        } else {
            id = artist.getInt("id");
        }
        artists.add(id);
        Database.addArtist(id, new Artist(id, name, songKickUrl, onTour));
    }

    private static void makeIds()   {
        ids = new int[artists.size()];
        for (int i = 0; i < artists.size(); i++)    {
            ids[i] = artists.get(i);
        }
    }

    public static void artistSelected(Artist artist, JSONObject songKick, JSONObject lastFM)
            throws JSONException   {
        artist.setConcerts(getConcerts(songKick));

        artist.setLastFM(true);
        getLastFmInfo(artist, lastFM);
    }

    static ArrayList<Concert> getConcerts(JSONObject songKick)  throws JSONException    {
        ArrayList<Concert> concertList = new ArrayList<>();

        JSONObject resultsPage = songKick.getJSONObject("resultsPage");
        if (resultsPage.getInt("totalEntries") > 0) {

            JSONObject results = resultsPage.getJSONObject("results");
            JSONArray concerts = results.getJSONArray("event");
            for (int i = 0; i < concerts.length(); i++)   {
                concertList.add(getConcert(concerts.getJSONObject(i)));
            }
        }

        return concertList;
    }

    private static Concert getConcert(JSONObject concert) throws JSONException  {
        Concert c;

        int concertID;
        String concertName;
        String city;
        Venue venue;
        String url;
        ArrayList<Artist> concertArtists;

        concertID = concert.getInt("id");
        concertName = concert.getString("displayName");
        url = concert.getString("uri");

        JSONObject location = concert.getJSONObject("location");
        city = location.getString("city");

        venue = getVenue(concert.getJSONObject("venue"));

        concertArtists = getArtists(concert.getJSONArray("performance"));

        c = new Concert(concertID, concertName, city, url, venue, concertArtists);

        Database.addConcert(concertID, c);

        return c;
    }

    private static Venue getVenue(JSONObject venue) throws JSONException    {
        Venue v;

        int venueID;
        String venueName;
        String url;

        if (!venue.isNull("id"))    {
            venueID = venue.getInt("id");
        } else {
            venueID = 0;
        }
        venueName = venue.getString("displayName");
        url = venue.getString("uri");
        v = new Venue(venueID, venueName, url);

        if (venueID > 0)    {
            Database.addVenue(venueID, v);
        }

        return v;
    }

    private static ArrayList<Artist> getArtists(JSONArray artists) throws JSONException    {
        ArrayList<Artist> artistList = new ArrayList<>();

        int artistId;
        String artistName;
        String artistUrl;

        for (int i = 0; i < artists.length(); i++)  {
            JSONObject artistRoot = artists.getJSONObject(i);
            JSONObject artist = artistRoot.getJSONObject("artist");
            artistId = artist.getInt("id");
            artistName = artist.getString("displayName");
            artistUrl = artist.getString("uri");

            Artist a = new Artist(artistId, artistName, artistUrl, true);

            Database.addArtist(artistId, a);

            artistList.add(a);
        }

        return artistList;
    }

    private static void getLastFmInfo(Artist artist, JSONObject lastFM) throws JSONException    {
        if (lastFM.has("artist"))   {
            ArrayList<String> simList = new ArrayList<>();
            JSONObject artistInfo = lastFM.getJSONObject("artist");
            JSONArray images = artistInfo.getJSONArray("image");
            JSONObject image = images.getJSONObject(3);
            artist.setImage(image.getString("#text"));

            JSONObject biography = artistInfo.getJSONObject("bio");
            artist.setBiography(biography.getString("summary"));

            JSONObject similar = artistInfo.getJSONObject("similar");
            JSONArray similarArtists = similar.getJSONArray("artist");
            for (int i = 0; i < similarArtists.length(); i++)   {
                JSONObject name = similarArtists.getJSONObject(i);
                simList.add(name.getString("name"));
            }
            artist.setSimArtists(simList);
        } else {
            artist.setImage(Constants.NO_IMAGE);
            artist.setBiography(Constants.NO_BIO);
            artist.setSimArtists(null);
        }
    }
}
