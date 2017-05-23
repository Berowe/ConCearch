package com.rowenetworks.concearch.tasks;

import android.os.AsyncTask;
import android.widget.Button;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.fragments.SearchFragment.OnSearchFragmentInteractionListener;
import com.rowenetworks.concearch.json_parsers.VenueSearchJSONParser;

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
 * The VenueSearchTask uses AsyncTask to retrieve an API response from the SongKick api when a user
 * searches for a venue.
 */

public class VenueSearchTask {
    private OnSearchFragmentInteractionListener mListener;
    private Button mSearchButton;

    public VenueSearchTask(OnSearchFragmentInteractionListener listener, String query,
                           Button searchButton) {
        mListener = listener;
        mSearchButton = searchButton;

        new GetVenueInfoTask().execute(query);
    }

    private class GetVenueInfoTask extends AsyncTask<String, Integer, int[]>    {
        @Override
        protected void onPreExecute()   {
            mSearchButton.setEnabled(false);
            mListener.onSearchProcess();
        }

        @Override
        protected int[] doInBackground(String... params) {
            int[] ids = new int[0];
            String songKickUrl = Constants.SONGKICK_VENUE_SEARCH_URL_BEGINNING
                    + params[0]
                    + Constants.SONGKICK_API_KEY;
            try {
                URL url = new URL(songKickUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream stream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                connection.disconnect();

                JSONObject songRoot = new JSONObject(builder.toString());
                ids = VenueSearchJSONParser.venueSearch(songRoot);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return ids;
        }

        @Override
        protected void onPostExecute(int[] ids) {
            mSearchButton.setEnabled(true);
            mListener.onFragmentInteraction(ids);
            mListener.onSearchProcess();
        }
    }
}
