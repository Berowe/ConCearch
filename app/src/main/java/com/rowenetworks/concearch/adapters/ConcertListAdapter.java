package com.rowenetworks.concearch.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.fragments.VenueDisplayFragment.OnVenueConcertSelected;
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.fragments.ArtistDisplayFragment.OnConcertSelectedListener;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * The ConcertListAdapter is for RecycleViews showing concerts that an artist is performing or venue
 * is hosting.
 */

public class ConcertListAdapter extends RecyclerView.Adapter<ConcertListAdapter.ConcertListHolder> {

    private Activity mActivity;
    private ArrayList<Concert> mConcerts;
    private OnConcertSelectedListener mArtistListener;
    private OnVenueConcertSelected mVenueListener;

    /**
     * First overloaded constructor used for ArtistDisplayFragment that sets up fragment/adapter
     * interaction.
     * @param activity The MainActivity used for the LinearLayoutManager in the holder class.
     * @param concerts The list of concerts to be viewed.
     * @param listener The listener for fragment/adapter interaction.
     */
    public ConcertListAdapter(Activity activity, ArrayList<Concert> concerts,
                              OnConcertSelectedListener listener)    {
        mActivity = activity;
        mConcerts = concerts;
        mArtistListener = listener;
    }

    /**
     * Second overloaded constructor used for VenueDisplayFragment that sets up fragment/adapter
     * interaction.
     * @param activity The MainActivity used for the LinearLayoutManager in the holder class.
     * @param concerts The list of concerts to be viewed.
     * @param listener The listener for fragment/adapter interaction.
     */
    public ConcertListAdapter(Activity activity, ArrayList<Concert> concerts,
                              OnVenueConcertSelected listener)    {
        mActivity = activity;
        mConcerts = concerts;
        mVenueListener = listener;
    }

    @Override
    public ConcertListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_concert_list, parent, false);
        return new ConcertListHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ConcertListHolder holder, int position) {
        final Concert concert = mConcerts.get(position);

        holder.mName.setText(concert.getName() + "\nVenue: " + concert.getVenue().getName());
        if (mArtistListener != null)    {
            holder.mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mArtistListener.onConcertSelected(concert);
                }
            });
        } else {
            holder.mName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mVenueListener.onVenueConcertSelected(concert);
                }
            });
        }
        holder.mAdapter = new SimpleListAdapter(concert);
        holder.mPerformers.setAdapter(holder.mAdapter);
    }

    @Override
    public int getItemCount() {
        return mConcerts.size();
    }

    class ConcertListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mName;
        private Button mExpand;
        private RecyclerView mPerformers;
        private SimpleListAdapter mAdapter;

        ConcertListHolder(View view) {
            super(view);

            mName = (TextView) view.findViewById(R.id.concert_list_textView);
            mExpand = (Button) view.findViewById(R.id.concert_list_show_artists_button);
            mExpand.setOnClickListener(this);
            mPerformers = (RecyclerView) view.findViewById(R.id.concert_list_recyclerView);
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            mPerformers.setLayoutManager(manager);
        }

        @Override
        public void onClick(View v) {
            if (mPerformers.getVisibility() == View.GONE)   {
                mPerformers.setVisibility(View.VISIBLE);
                mExpand.setText(R.string.hide_artists);
            } else {
                mPerformers.setVisibility(View.GONE);
                mExpand.setText(R.string.show_artists);
            }
        }
    }
}
