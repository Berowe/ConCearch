package com.rowenetworks.concearch.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rowenetworks.concearch.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.model.Artist;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 * RecyclerView adapter to show the artist's name and if the artist is on tour or not.  Clicked
 * artist in list will send the recycler the Artist object.
 */

public class ArtistListAdapter extends RecyclerView.Adapter<ArtistListAdapter.ArtistListHolder> {

    private ArrayList<Artist> mArtists;
    private OnClickListener mListener;

    public ArtistListAdapter(ArrayList<Artist> artists)    {
        mArtists = artists;
    }

    @Override
    public ArtistListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_artist_list, parent, false);
        return new ArtistListHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ArtistListHolder holder, int position) {
        Artist artist = mArtists.get(position);
        holder.mName.setText(artist.getName());
        holder.mId.setText(String.valueOf(artist.getId()));
        if (!artist.getOnTour()) {
            Drawable tour = holder.mContext.getDrawable(R.drawable.not_on_tour_custom_background);
            holder.mOnTour.setBackground(tour);
            holder.mOnTour.setText(R.string.not_onTour);
        }
    }

    public interface OnClickListener    {
        void onClick(Artist artist);
    }

    public void setOnClickListener(final OnClickListener listener)  {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position)    {
        return position;
    }

    @Override
    public int getItemCount() {
        return mArtists.size();
    }

    class ArtistListHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {

        private TextView mName;
        private TextView mOnTour;
        private TextView mId;
        private Context mContext;

        ArtistListHolder (View view)    {
            super(view);
            mName = (TextView) view.findViewById(R.id.artist_list_name_textView);
            mName.setOnClickListener(this);
            mOnTour = (TextView) view.findViewById(R.id.artist_list_onTour_textView);
            mId = (TextView) view.findViewById(R.id.artist_id_textView);
            mContext = view.getContext();
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(Database.getArtist(Integer.parseInt(mId.getText().toString())));
        }
    }
}
