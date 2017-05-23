package com.rowenetworks.concearch.json_parsers;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.tools.Database;
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
 * Takes in a JSON object and parses it for information on artists based off a user's search query.
 */

public class ArtistSearchJSONParser {

    private static int[] ids;
    private static ArrayList<Integer> artists;

    /**
     * The initial call to get information based off the user's search query.
     * @param songKick The JSON object returned by the SongKick api.
     * @return The IDs of the Artist objects.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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

    /**
     * Helper method to parse the array of artists in the SongKick api response.
     * @param artists The JSON array of artists.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
    private static void parseArray(JSONArray artists) throws JSONException  {

        for (int i = 0; i < artists.length(); i++)  {
            JSONObject artist = artists.getJSONObject(i);
            parseArtist(artist);
        }
    }

    /**
     * Helper method to parse each artist in the array of artists passed to parseArray method.
     * @param artist The JSON object of the artist to parse.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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

    /**
     * Helper method to develop the int array to return to the activity.
     */
    private static void makeIds()   {
        ids = new int[artists.size()];
        for (int i = 0; i < artists.size(); i++)    {
            ids[i] = artists.get(i);
        }
    }

    /**
     * This is the secondary call made when the user selects an artist from the list of search
     * results.
     * @param artist The artist that the user selected.
     * @param songKick The JSON object returned by the SongKick api response.
     * @param lastFM The JSON object returned by the LastFM api response.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
    public static void artistSelected(Artist artist, JSONObject songKick, JSONObject lastFM)
            throws JSONException   {
        artist.setConcerts(getConcerts(songKick));

        artist.setLastFM(true);
        getLastFmInfo(artist, lastFM);
    }

    /**
     * Helper method to get concert information from a SongKick JSON object.
     * @param songKick The JSON object from the SongKick api response.
     * @return Returns an ArrayList of concerts.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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

    /**
     * Takes each concert object from the getConcerts method and parses it.
     * @param concert The JSON object from the Concert array.
     * @return Returns the Concert
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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

    /**
     * Takes the venue object from the getConcert method and parses it.
     * @param venue The JSON object within the Concert JSON object.
     * @return Returns the Venue object.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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

    /**
     * Takes the JSON array of artists performing at the concert and parses it.
     * @param artists The JSON array of artist perfomring.
     * @return An ArrayList of Artist object.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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

    /**
     * Takes the JSON object from the LastFM and parses it.
     * @param artist The artist to update with the LastFM data.
     * @param lastFM The JSON object from the LastFM api response.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
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
