package com.rowenetworks.concearch.tasks;

import android.os.AsyncTask;

import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.fragments.ArtistResultsFragment.OnArtistSelectedListener;
import com.rowenetworks.concearch.json_parsers.ArtistSearchJSONParser;
import com.rowenetworks.concearch.model.Artist;

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

public class ArtistDetailsTask {

    private Artist mArtist;
    private OnArtistSelectedListener mListener;

    public ArtistDetailsTask(Artist artist, OnArtistSelectedListener listener)  {
        mArtist = artist;
        mListener = listener;

        new GetArtistInfoTask().execute(mArtist.getId());
    }

    private class GetArtistInfoTask extends AsyncTask<Integer, Integer, Artist> {

        @Override
        protected void onPreExecute()   {
            mListener.onArtistProcess();
        }

        @Override
        protected Artist doInBackground(Integer... params) {

            if (!mArtist.checkLastFm()) {
                String name = mArtist.getName();
                String songKickUrl = Constants.SONGKICK_SELECTED_ARTIST_URL_BEGINNING + params[0]
                        + Constants.SONGKICK_SELECTED_ARTIST_URL_END;

                String lastFmUrl = Constants.LASTFM_URL_BEGINNING + name
                        + Constants.LASTFM_URL_END;

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

                    JSONObject songRoot = new JSONObject(builder.toString());

                    url = new URL(lastFmUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    stream = new BufferedInputStream(connection.getInputStream());
                    bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    builder = new StringBuilder();
                    while((inputString = bufferedReader.readLine()) != null)    {
                        builder.append(inputString);
                    }

                    JSONObject lastRoot = new JSONObject(builder.toString());

                    ArtistSearchJSONParser.artistSelected(mArtist, songRoot, lastRoot);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }

            return mArtist;
        }

        @Override
        protected void onPostExecute(Artist artist) {
            mListener.onArtistSelected(artist);
            mListener.onArtistProcess();
        }
    }
}
