package com.rowenetworks.concearch.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rowenetworks.concearch.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.model.Venue;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 */

public class VenueListAdapter extends RecyclerView.Adapter<VenueListAdapter.VenueListHolder>    {

    private OnClickListener mListener;
    private ArrayList<Venue> mVenues;

    public VenueListAdapter(ArrayList<Venue> venues)    { mVenues = venues; }

    @Override
    public VenueListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_venue_list, parent, false);
        return new VenueListHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(VenueListHolder holder, int position) {
        Venue venue = mVenues.get(position);
        holder.nameTextView.setText(venue.getName());
        holder.locationTextView.setText(venue.getAddress());
        holder.phoneTextView.setText(venue.getPhone());
        holder.websiteTextView.setText(venue.getUrl());
        holder.idTextView.setText(String.valueOf(venue.getId()));
    }

    @Override
    public int getItemViewType(int position)    {
        return position;
    }

    @Override
    public int getItemCount() {
        return mVenues.size();
    }

    public interface OnClickListener    { void onClick(Venue venue); }

    public void setOnClickListener(final OnClickListener listener)  {
        mListener = listener;
    }

    class VenueListHolder extends RecyclerView.ViewHolder implements View.OnClickListener   {
        private TextView nameTextView;
        private TextView locationTextView;
        private TextView phoneTextView;
        private TextView websiteTextView;
        private Button detailsButton;
        private TextView idTextView;

        VenueListHolder(View view)  {
            super(view);
            nameTextView = (TextView) view.findViewById(R.id.venue_results_name_textView);
            locationTextView = (TextView) view.findViewById(R.id.venue_results_location_textView);
            phoneTextView = (TextView) view.findViewById(R.id.venue_results_phone_textView);
            websiteTextView = (TextView) view.findViewById(R.id.venue_results_website_textView);
            idTextView = (TextView) view.findViewById(R.id.venue_results_id_textView);
            detailsButton = (Button) view.findViewById(R.id.venue_results_button);
            detailsButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(Database.getVenue(Integer.parseInt(idTextView.getText().toString())));
        }
    }
}
