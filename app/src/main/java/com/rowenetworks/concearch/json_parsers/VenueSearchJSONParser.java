package com.rowenetworks.concearch.json_parsers;

import com.rowenetworks.concearch.tools.Database;
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.model.Venue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * This class takes the initial venue search results JSON Object and parses it.
 */

public class VenueSearchJSONParser {

    private static final String NAME = "displayName";
    private static final String COMMA = ", ";

    private static int[] ids;
    private static ArrayList<Integer> venues;

    /**
     * The initial call for Venue information from a user's search query.
     * @param songKick The JSON object returned by the SongKick api response.
     * @return Returns the list of IDs of Venue objects.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
    public static int[] venueSearch(JSONObject songKick) throws JSONException {

        venues = new ArrayList<>();
        JSONObject resultsPage = songKick.getJSONObject("resultsPage");

        if (resultsPage.getInt("totalEntries") > 0) {
            JSONObject results = resultsPage.getJSONObject("results");
            JSONArray venues = results.getJSONArray("venue");

            parseArray(venues);
            makeIds();
        } else {
            ids = new int[0];
        }

        return ids;
    }

    /**
     * Parses the array of venues from the JSON array from the venueSearch method.
     * @param venueArray The JSON array of venues.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
    private static void parseArray(JSONArray venueArray) throws JSONException   {
        for (int i = 0; i < venueArray.length(); i++)  {
            JSONObject venue = venueArray.getJSONObject(i);
            parseVenue(venue);
        }
    }

    /**
     * Parses each venue from the array of venues from the parseArray method.
     * @param venue The JSON object of the venue.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
    private static void parseVenue(JSONObject venue) throws JSONException   {
        int id = venue.getInt("id");
        String name = venue.getString(NAME);
        String phone;
        if (!venue.getString("phone").equals("null")) {
            phone = venue.getString("phone");
        } else {
            phone = "No Phone Number Provided";
        }
        String website;
        if (!venue.getString("website").equals("null"))   {
            website = venue.getString("website");
        } else {
            website = "No Website Provided";
        }
        String address;
        StringBuilder builder = new StringBuilder();
        if (venue.getString("street").equals(""))   {
            builder.append(name);
        } else {
            builder.append(venue.get("street"));
        }
        if (venue.has("city"))  {
            JSONObject city = venue.getJSONObject("city");
            builder.append(COMMA);
            builder.append(city.getString(NAME));

            if (city.has("state"))  {
                JSONObject stateObject = city.getJSONObject("state");
                builder.append(COMMA);
                builder.append(stateObject.getString(NAME));
            }
            if (city.has("country"))    {
                JSONObject countryObject = city.getJSONObject("country");
                builder.append(COMMA);
                builder.append(countryObject.getString(NAME));
            }
        }
        address = builder.toString();

        Venue v = new Venue(id, name, address, phone, website);
        Database.addVenue(id, v);

        venues.add(id);
    }

    /**
     * Helper method to create the int array to return to the VenueSearchTask.
     */
    private static void makeIds()  {
        ids = new int[venues.size()];
        for (int i = 0; i < venues.size(); i++) {
            ids[i] = venues.get(i);
        }
    }

    /**
     * Method to get the list of concerts at a venue when the user selects a venue from the search
     * results.
     * @param songKick The JSON Object from the SongKick api response.
     * @return Returns an ArrayList of concerts.
     * @throws JSONException If retrieving data from the JSON object fails.
     */
    public static ArrayList<Concert> getConcerts(JSONObject songKick)  throws JSONException    {
        ArrayList<Concert> concertList = new ArrayList<>();

        JSONObject resultsPage = songKick.getJSONObject("resultsPage");
        if (resultsPage.getInt("totalEntries") > 0) {
            concertList = ArtistSearchJSONParser.getConcerts(songKick);
        }

        return concertList;
    }


}
