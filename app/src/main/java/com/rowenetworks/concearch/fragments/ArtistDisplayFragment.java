package com.rowenetworks.concearch.fragments;


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

import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.adapters.ConcertListAdapter;
import com.rowenetworks.concearch.adapters.SimpleArtistListAdapter;
import com.rowenetworks.concearch.model.Artist;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistDisplayFragment extends Fragment implements View.OnClickListener {

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
    SimpleArtistListAdapter simArtistAdapter;
    ConcertListAdapter concertAdapter;


    public ArtistDisplayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_display, container, false);

        artist = (Artist) getArguments().getSerializable(Constants.ARTIST_OBJECT_KEY);

        artistImage = (ImageView) view.findViewById(R.id.artist_imageView);
        biographyButton = (Button) view.findViewById(R.id.artist_biography_button);
        similarButton = (Button) view.findViewById(R.id.artist_similar_artist_button);
        concertsButton = (Button) view.findViewById(R.id.artist_concerts_button);
        biography = (TextView) view.findViewById(R.id.artist_biography_textView);
        similarArtists = (RecyclerView) view.findViewById(R.id.similar_artist_recycler);
        concerts = (RecyclerView) view.findViewById(R.id.artist_concerts_recycler);
        artistManager = new LinearLayoutManager(this.getActivity());
        concertManager = new LinearLayoutManager(this.getActivity());
        setFields();

        return view;
    }

    private void setFields()    {
        setImage();
        setButtons();
        setBiography();
        setSimilarArtistRecycler();
        setConcertsRecycler();
    }

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

    private void setButtons()   {
        biographyButton.setOnClickListener(this);
        similarButton.setOnClickListener(this);
        concertsButton.setOnClickListener(this);
    }

    private void setBiography() {
        Spanned spanned = Html.fromHtml(artist.getBiography(), Html.FROM_HTML_MODE_LEGACY);
        biography.setText(spanned);
        biography.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void setSimilarArtistRecycler()  {
        simArtistAdapter = new SimpleArtistListAdapter(artist);
        similarArtists.setLayoutManager(artistManager);
        similarArtists.setAdapter(simArtistAdapter);
    }

    private void setConcertsRecycler()  {
        concertAdapter = new ConcertListAdapter(this.getActivity(), artist.getConcerts());
        concerts.setLayoutManager(concertManager);
        concerts.setAdapter(concertAdapter);
    }

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
