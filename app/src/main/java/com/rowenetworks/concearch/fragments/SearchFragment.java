package com.rowenetworks.concearch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.tasks.ArtistSearchTask;
import com.rowenetworks.concearch.tasks.VenueSearchTask;

/**
 * @author Braxton Rowe
 * @version 1.0
 * The SearchFragment is where the user queries artists or venues.
 */
public class SearchFragment extends Fragment implements View.OnClickListener    {

    private OnSearchFragmentInteractionListener mListener;

    EditText mSearchEditText;
    RadioGroup mRadioGroup;
    RadioButton mArtistButton;
    RadioButton mVenueButton;
    RadioButton mLocationButton;
    Button mSearchButton;
    Button mShowSearchButton;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchEditText = (EditText) view.findViewById(R.id.search_editText);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.search_radioGroup);
        mArtistButton = (RadioButton) view.findViewById(R.id.artist_radioButton);
        mVenueButton = (RadioButton) view.findViewById(R.id.venue_radioButton);
        mSearchButton = (Button) view.findViewById(R.id.search_button);
        mSearchButton.setOnClickListener(this);
        mShowSearchButton = (Button) view.findViewById(R.id.show_search_button);
        mShowSearchButton.setOnClickListener(this);

        return view;
    }

    /**
     * Called when the user has clicked the search button.
     * @param query The user's search query.
     */
    private void onSearchPressed(String query) {
        mSearchButton.setEnabled(false);
        hideSearch();
        if (mListener != null) {
            switch (mRadioGroup.getCheckedRadioButtonId())  {
                case R.id.artist_radioButton:
                    new ArtistSearchTask(mListener, query, mSearchButton);
                    break;
                case R.id.venue_radioButton:
                    new VenueSearchTask(mListener, query, mSearchButton);
                    break;
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchFragmentInteractionListener) {
            mListener = (OnSearchFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.show_search_button)   {
            showSearch();
        } else {
            onSearchPressed(mSearchEditText.getText().toString());
        }
    }

    /**
     * Called when the user clicks the Show Search button.  This button is only visible when the
     * rest of the search fragment is not visible.
     */
    private void showSearch()   {
        mShowSearchButton.setVisibility(View.GONE);
        mSearchButton.setVisibility(View.VISIBLE);
        mSearchEditText.setVisibility(View.VISIBLE);
        mRadioGroup.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the user clicks the Search button.  The show search button is only visible when
     * the rest of the search fragment is not visible.
     */
    private void hideSearch()   {
        mSearchButton.setVisibility(View.GONE);
        mSearchEditText.setVisibility(View.GONE);
        mRadioGroup.setVisibility(View.GONE);
        mShowSearchButton.setVisibility(View.VISIBLE);
    }

    /**
     * This interface is called by the ArtistDetailsTask to interact with the MainActivity.
     */
    public interface OnSearchFragmentInteractionListener {
        void onFragmentInteraction(int[] ids);
        void onSearchProcess();
    }
}