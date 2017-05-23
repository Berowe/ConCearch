package com.rowenetworks.concearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.tools.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.adapters.VenueListAdapter;
import com.rowenetworks.concearch.model.Venue;
import com.rowenetworks.concearch.tasks.VenueDetailsTask;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * VenueResultsFragment shows a list of venues based on the user's search query.
 */
public class VenueResultsFragment extends Fragment implements VenueListAdapter.OnClickListener  {

    private OnVenueSelected mListener;
    private RecyclerView mRecycler;
    private ArrayList<Venue> mVenues;
    VenueListAdapter mAdapter;

    public VenueResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venue_results, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.venue_results_recycleView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        updateList(getArguments().getIntArray(Constants.LIST_KEY));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVenueSelected) {
            mListener = (OnVenueSelected) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnVenueSelected");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Updates the list of venues based off the user's search query.
     * @param ids The IDs of venues to get Venue objects from the Database.
     */
    public void updateList(int[] ids)    {
        makeList(ids);
        mAdapter = new VenueListAdapter(mVenues);
        mAdapter.setOnClickListener(this);
        mRecycler.setAdapter(mAdapter);
    }

    /**
     * Helper method to make an ArrayList for the VenueListAdapter.
     * @param ids The IDs of venues to get Venue objects from the Database.
     */
    private void makeList(int[] ids)    {
        mVenues = new ArrayList<>();
        for (int id: ids) {
            mVenues.add(Database.getVenue(id));
        }
    }

    /**
     * Methods called by the VenueDetailsTask for interaction with the MainActivity.
     */
    public interface OnVenueSelected {
        void onVenueSelected(Venue venue);
        void onVenueProcess();
    }

    @Override
    public void onClick(Venue venue) {
        new VenueDetailsTask(venue, mListener);
    }

}