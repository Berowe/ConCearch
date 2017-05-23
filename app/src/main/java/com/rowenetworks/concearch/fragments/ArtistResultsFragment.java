package com.rowenetworks.concearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.tools.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.adapters.ArtistListAdapter;
import com.rowenetworks.concearch.model.Artist;
import com.rowenetworks.concearch.tasks.ArtistDetailsTask;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * The ArtistResultsFragment shows a list of artists pertaining to a user's search query.
 */
public class ArtistResultsFragment extends Fragment implements View.OnClickListener,
        ArtistListAdapter.OnClickListener   {

    private OnArtistSelectedListener mListener;

    private CheckBox mCheckbox;
    private RecyclerView mRecycler;
    private ArtistListAdapter mAdapter;
    private ArtistListAdapter mOnTourAdapter;
    ArrayList<Artist> mAllArtists;
    ArrayList<Artist> mOnTourArtists;


    public ArtistResultsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_results, container, false);

        int[] list = getArguments().getIntArray(Constants.LIST_KEY);
        mRecycler = (RecyclerView) view.findViewById(R.id.artist_results_recyclerView);
        mRecycler.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mCheckbox = (CheckBox) view.findViewById(R.id.artist_results_checkBox);
        mCheckbox.setOnClickListener(this);
        setLists(list);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArtistSelectedListener) {
            mListener = (OnArtistSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Interface for fragment interaction.  Called by ArtistDetailsTask.
     */
    public interface OnArtistSelectedListener   {
        void onArtistSelected(Artist artist);
        void onArtistProcess();
    }

    /**
     * Method to set the two lists.  One for all artists pertaining to the user's search query, one
     * for only artists who are on tour.
     * @param ids The IDs of the artists for Database reference.
     */
    public void setLists(int[] ids)    {
        mAllArtists = new ArrayList<>();
        mOnTourArtists = new ArrayList<>();
        for (int key: ids)  {
            Artist artist = Database.getArtist(key);
            mAllArtists.add(artist);
            if (artist.getOnTour()) {
                mOnTourArtists.add(artist);
            }
        }
        mAdapter = new ArtistListAdapter(mAllArtists);
        mAdapter.setOnClickListener(this);
        mOnTourAdapter = new ArtistListAdapter(mOnTourArtists);
        mOnTourAdapter.setOnClickListener(this);
        switchLists(mCheckbox.isChecked());
    }

    /**
     * Method called when the user checks or unchecks the mCheckbox for all artists or on tour
     * artists.
     * @param checked mCheckbox checked state.
     */
    private void switchLists(boolean checked)  {
        if (checked)    {
            mRecycler.setAdapter(mOnTourAdapter);
        } else {
            mRecycler.setAdapter(mAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        switchLists(mCheckbox.isChecked());
    }

    @Override
    public void onClick(Artist artist) { new ArtistDetailsTask(artist, mListener); }
}
