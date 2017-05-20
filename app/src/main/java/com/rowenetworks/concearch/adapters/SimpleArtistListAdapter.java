package com.rowenetworks.concearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.model.Artist;
import com.rowenetworks.concearch.model.Concert;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 */

public class SimpleArtistListAdapter extends
        RecyclerView.Adapter<SimpleArtistListAdapter.SimArtistListHolder> {

    private ArrayList<String> artistNames;

    public SimpleArtistListAdapter(Artist artist) {
        artistNames = artist.getSimilarArtists();
    }

    SimpleArtistListAdapter(Concert concert) {
        ArrayList<Artist> artists = concert.getArtists();
        artistNames = new ArrayList<>();
        for (Artist artist : artists) {
            artistNames.add(artist.getName());
        }
    }

    @Override
    public SimArtistListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_simple_artist_list, parent, false);
        return new SimArtistListHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(SimArtistListHolder holder, int position) {
        holder.mNameView.setText(artistNames.get(position));
    }

    @Override
    public int getItemViewType(int position)    {
        return position;
    }

    @Override
    public int getItemCount() {
        return artistNames.size();
    }

    class SimArtistListHolder extends RecyclerView.ViewHolder   {

        private TextView mNameView;

        SimArtistListHolder (View view) {
            super(view);

            mNameView = (TextView) view.findViewById(R.id.similar_artist_list_textView);
        }
    }
}
