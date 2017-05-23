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
 * The SimpleListAdapter is for RecycleViews that only need to show the artist's name.
 */

public class SimpleListAdapter extends
        RecyclerView.Adapter<SimpleListAdapter.SimArtistListHolder> {

    private ArrayList<String> mList;

    /**
     * First overloaded constructor for an artist's similar artist list.
     * @param artist The artist the user selected.
     */
    public SimpleListAdapter(Artist artist) {
        mList = artist.getSimilarArtists();
    }

    /**
     * Second overloaded constructor for a concert's performing artist list.
     * @param concert The concert the user selected or is shown in a list of concerts.
     */
    public SimpleListAdapter(Concert concert) {
        ArrayList<Artist> artists = concert.getArtists();
        mList = new ArrayList<>();
        for (Artist artist : artists) {
            mList.add(artist.getName());
        }
    }

    @Override
    public SimArtistListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_simple_list, parent, false);
        return new SimArtistListHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(SimArtistListHolder holder, int position) {
        holder.mNameView.setText(mList.get(position));
    }

    @Override
    public int getItemViewType(int position)    {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SimArtistListHolder extends RecyclerView.ViewHolder   {

        /**
         * This TextView is the only field with onClick called from the XML layout.
         */
        private TextView mNameView;

        SimArtistListHolder (View view) {
            super(view);
            mNameView = (TextView) view.findViewById(R.id.simple_list_textView);
        }
    }
}
