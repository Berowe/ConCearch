package com.rowenetworks.concearch.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.adapters.ConcertListAdapter;
import com.rowenetworks.concearch.adapters.SimpleListAdapter;
import com.rowenetworks.concearch.model.Artist;
import com.rowenetworks.concearch.model.Concert;
import com.squareup.picasso.Picasso;

/**
 * @author Braxton Rowe
 * @version 1.0
 * Fragment to show a specific artist that the user has selected from the artist search results.
 */
public class ArtistDisplayFragment extends Fragment implements View.OnClickListener {

    OnConcertSelectedListener mListener;

    Artist artist;
    ImageView artistImage;
    Button biographyButton;
    Button similarButton;
    Button concertsButton;
    TextView biography;
    RecyclerView similarArtists;
    RecyclerView concerts;
    LinearLayoutManager artistManager;
    LinearLayoutManager concertManager;
    SimpleListAdapter simArtistAdapter;
    ConcertListAdapter concertAdapter;


    public ArtistDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_display, container, false);

        artist = (Artist) getArguments().getSerializable(Constants.ARTIST_OBJECT_KEY);

        setView(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnConcertSelectedListener) {
            mListener = (OnConcertSelectedListener) context;
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

    /**
     * This interface is used in the MainActivity to show the concert information selected by the
     * user.
     */
    public interface OnConcertSelectedListener  {
        void onConcertSelected(Concert concert);
    }

    /**
     * Shows the informaiton of the Artist.  There are times when the information does not populate
     * and so the method calls itself again if the biography field is null.
     * @param view The fragment's view object.
     */
    private void setView(View view)  {
        artistImage = (ImageView) view.findViewById(R.id.artist_imageView);
        biographyButton = (Button) view.findViewById(R.id.artist_biography_button);
        similarButton = (Button) view.findViewById(R.id.artist_similar_artist_button);
        concertsButton = (Button) view.findViewById(R.id.artist_concerts_button);
        biography = (TextView) view.findViewById(R.id.artist_biography_textView);
        similarArtists = (RecyclerView) view.findViewById(R.id.similar_artist_recycler);
        concerts = (RecyclerView) view.findViewById(R.id.artist_concerts_recycler);
        artistManager = new LinearLayoutManager(this.getActivity());
        concertManager = new LinearLayoutManager(this.getActivity());
        if (biography != null)  {
            setFields();
        } else {
            setView(view);
        }
    }

    /**
     * Helper method to set the fields in the fragment's view.
     */
    private void setFields()    {
        setImage();
        setButtons();
        setBiography();
        setSimilarArtistRecycler();
        setConcertsRecycler();
    }

    /**
     * Helper method to set the ImageView for the artist.
     */
    private void setImage() {
        try{
            if (!artist.getImageUrl().equals("")) {
                Picasso.with(getActivity())
                        .load(artist.getImageUrl())
                        .into(artistImage);
            } else {
                Picasso.with(getActivity())
                        .load(R.drawable.iconimage).resize(150, 150).into(artistImage);
            }
        } catch (NullPointerException e)    {
            Picasso.with(getActivity())
                    .load(R.drawable.iconimage).resize(150, 150).into(artistImage);
        }
    }

    /**
     * Helper method to set the buttons' onClickListeners for the fragment's buttons.
     */
    private void setButtons()   {
        biographyButton.setOnClickListener(this);
        similarButton.setOnClickListener(this);
        concertsButton.setOnClickListener(this);
    }

    /**
     * Sets the TextView with the artist's biography.  The biography comes from LastFM api and has
     * HTML code embedded that needs to be shown properly.  The setMovementMethod allows for that
     * HTML link to open the phone's browser with the HTML link provided by LastFM.
     */
    private void setBiography() {
        Spanned spanned = Html.fromHtml(artist.getBiography(), Html.FROM_HTML_MODE_LEGACY);
        if (biography != null)  {
            biography.setText(spanned);
            biography.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * Helper method to set the similar artist list.
     */
    private void setSimilarArtistRecycler()  {
        simArtistAdapter = new SimpleListAdapter(artist);
        similarArtists.setLayoutManager(artistManager);
        similarArtists.setAdapter(simArtistAdapter);
    }

    /**
     * Helper method to set the artist's upcoming concert list.
     */
    private void setConcertsRecycler()  {
        concertAdapter = new ConcertListAdapter(this.getActivity(), artist.getConcerts(),
                mListener);
        concerts.setLayoutManager(concertManager);
        concerts.setAdapter(concertAdapter);
    }

    /**
     * Shows and hides TextViews based on what the user has selected.  In the XML layout, these
     * TextViews all overlap.  The artist's biography is visible by default.
     * @param v The button clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.artist_biography_button:
                biographyButton.setEnabled(false);
                similarButton.setEnabled(true);
                concertsButton.setEnabled(true);
                biography.setVisibility(View.VISIBLE);
                similarArtists.setVisibility(View.GONE);
                concerts.setVisibility(View.GONE);
                break;

            case R.id.artist_similar_artist_button:
                biographyButton.setEnabled(true);
                similarButton.setEnabled(false);
                concertsButton.setEnabled(true);
                biography.setVisibility(View.GONE);
                similarArtists.setVisibility(View.VISIBLE);
                concerts.setVisibility(View.GONE);
                break;

            case R.id.artist_concerts_button:
                biographyButton.setEnabled(true);
                similarButton.setEnabled(true);
                concertsButton.setEnabled(false);
                biography.setVisibility(View.GONE);
                similarArtists.setVisibility(View.GONE);
                concerts.setVisibility(View.VISIBLE);
                break;
        }
    }
}
