package com.rowenetworks.concearch.tasks;

import android.os.AsyncTask;
import android.widget.Button;

import com.rowenetworks.concearch.json_parsers.ArtistSearchJSONParser;
import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.fragments.SearchFragment.OnSearchFragmentInteractionListener;

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
 * This task is run when the user searches for an artist.
 */

public class ArtistSearchTask {
    private OnSearchFragmentInteractionListener mListener;
    private Button mSearchButton;

    public ArtistSearchTask(OnSearchFragmentInteractionListener listener, String artist,
                            Button button)    {
        mListener = listener;
        mSearchButton = button;
        new GetArtistInfoTask().execute(artist);
    }

    private class GetArtistInfoTask extends AsyncTask<String, Integer, int[]>   {

        @Override
        protected void onPreExecute()  {
            mSearchButton.setEnabled(false);
            mListener.onSearchProcess();
        }

        @Override
        protected int[] doInBackground(String... params) {
            int[] ids = new int[0];
            String songKickUrl = Constants.SONGKICK_ARTIST_SEARCH_URL_BEGINNING
                    + params[0]
                    + Constants.SONGKICK_API_KEY;
            try {
                URL url = new URL(songKickUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                InputStream stream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();
                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                connection.disconnect();

                JSONObject songRoot = new JSONObject(builder.toString());
                ids = ArtistSearchJSONParser.artistSearch(songRoot);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return ids;
        }

        @Override
        protected void onPostExecute(int[] ids) {
            mListener.onFragmentInteraction(ids);
            mSearchButton.setEnabled(true);
            mListener.onSearchProcess();
        }
    }
}
