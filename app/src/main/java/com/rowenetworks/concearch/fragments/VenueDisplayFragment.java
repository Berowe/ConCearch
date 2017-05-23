package com.rowenetworks.concearch.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.adapters.ConcertListAdapter;
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.model.Venue;

/**
 * A simple {@link Fragment} subclass.
 */
public class VenueDisplayFragment extends Fragment  {

    private OnVenueConcertSelected mListener;

    public VenueDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_display, container, false);

        Venue venue = (Venue) getArguments().getSerializable(Constants.VENUE_OBJECT_KEY);
        if (venue != null)  {
            TextView name = (TextView) view.findViewById(R.id.venue_display_name_textView);
            name.setText(venue.getName());
            TextView location = (TextView) view.findViewById(R.id.venue_display_location_textView);
            location.setText(venue.getAddress());
            TextView phone = (TextView) view.findViewById(R.id.venue_display_phone_textView);
            phone.setText(venue.getPhone());
            TextView website = (TextView) view.findViewById(R.id.venue_display_website_textView);
            website.setText(venue.getUrl());

            RecyclerView recylcler =
                    (RecyclerView) view.findViewById(R.id.venue_display_recycleView);
            recylcler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
            recylcler.setAdapter(new ConcertListAdapter(this.getActivity(), venue.getConcerts(),
                    mListener));
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVenueConcertSelected) {
            mListener = (OnVenueConcertSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnConcertSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnVenueConcertSelected {
        void onVenueConcertSelected(Concert concert);
    }
}
