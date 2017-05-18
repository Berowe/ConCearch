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
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.model.Venue;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * @author Braxton Rowe
 * @version 1.0
 */

public class ConcertListAdapter extends RecyclerView.Adapter<ConcertListAdapter.ConcertListHolder> {

    Activity mActivity;
    ArrayList<Concert> mConcerts;

    public ConcertListAdapter(Activity activity, ArrayList<Concert> concerts)    {
        mActivity = activity;
        mConcerts = concerts;
    }

    @Override
    public ConcertListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_concert_list, parent, false);
        return new ConcertListHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ConcertListHolder holder, int position) {
        Concert concert = mConcerts.get(position);

        holder.mName.setText(concert.getName() + "\nVenue: " + concert.getVenue().getName());
        holder.mAdapter = new SimpleArtistListAdapter(concert);
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
        private SimpleArtistListAdapter mAdapter;

        public ConcertListHolder(View view) {
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
