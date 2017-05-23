package com.rowenetworks.concearch.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.adapters.SimpleListAdapter;
import com.rowenetworks.concearch.model.Concert;

/**
 * @author Braxton Rowe
 * @version 1.0
 * The ConcertDisplayFragment shows the concert selected by the user.
 */
public class ConcertDisplayFragment extends Fragment {


    public ConcertDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_concert_display, container, false);

        Concert concert = (Concert) getArguments().getSerializable(Constants.CONCERT_OBJECT_KEY);

        TextView concertName = (TextView) view.findViewById(R.id.concert_display_name_textView);
        TextView venueName = (TextView) view.findViewById(R.id.concert_display_venueName_textView);
        TextView concertWebsite = (TextView)
                view.findViewById(R.id.concert_display_website_textView);
        RecyclerView artistList = (RecyclerView)
                view.findViewById(R.id.concert_display_recycleView);

        if (concert != null)    {
            concertName.setText(concert.getName());
            venueName.setText(concert.getVenue().getName());
            concertWebsite.setText(concert.getWebsite());

            LinearLayoutManager manager = new LinearLayoutManager(this.getActivity());
            SimpleListAdapter adapter = new SimpleListAdapter(concert);
            artistList.setLayoutManager(manager);
            artistList.setAdapter(adapter);
        }

        return view;
    }

}
