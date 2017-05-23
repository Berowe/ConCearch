package com.rowenetworks.concearch.tasks;

import android.os.AsyncTask;

import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.fragments.VenueResultsFragment.OnVenueSelected;
import com.rowenetworks.concearch.json_parsers.ArtistSearchJSONParser;
import com.rowenetworks.concearch.json_parsers.VenueSearchJSONParser;
import com.rowenetworks.concearch.model.Venue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Braxton Rowe
 * @version 1.0
 */

public class VenueDetailsTask {

    private OnVenueSelected mListener;
    private Venue mVenue;

    public VenueDetailsTask(Venue venue, OnVenueSelected listener)  {
        mVenue = venue;
        mListener = listener;

        new GetVenueDetailsTask().execute(mVenue);
    }


    private class GetVenueDetailsTask extends AsyncTask<Venue, Integer, Venue>  {
        @Override
        protected void onPreExecute()   { mListener.onVenueProcess(); }

        @Override
        protected Venue doInBackground(Venue... params) {
            mVenue = params[0];

            String songKickUrl = Constants.SONGKICK_VENUE_DETAILS_SEARCH_URL_BEGINNING
                    + String.valueOf(mVenue.getId())
                    + Constants.SONGKICK_VENUE_DETAILS_SEARCH_URL_END;

            try {
                URL url = new URL(songKickUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                connection.disconnect();

                JSONObject songRoot = new JSONObject(builder.toString());
                mVenue.setConcerts(VenueSearchJSONParser.getConcerts(songRoot));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return mVenue;
        }

        @Override
        protected void onPostExecute(Venue venue)   {
            mListener.onVenueProcess();
            mListener.onVenueSelected(venue);
        }
    }
}
