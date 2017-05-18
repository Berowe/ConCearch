package com.rowenetworks.concearch.activities;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.rowenetworks.concearch.Constants;
import com.rowenetworks.concearch.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.fragments.ArtistDisplayFragment;
import com.rowenetworks.concearch.fragments.ArtistResultsFragment;
import com.rowenetworks.concearch.fragments.SearchFragment;
import com.rowenetworks.concearch.model.Artist;

public class MainActivity extends AppCompatActivity implements
        SearchFragment.OnSearchFragmentInteractionListener,
        ArtistResultsFragment.OnArtistSelectedListener  {

    SearchFragment searchFragment;
    LinearLayout mainLayout;
    FrameLayout lowerFrame;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainLayout = (LinearLayout) findViewById(R.id.main_activity_container);
        Database.initializeDatabase();
        lowerFrame = (FrameLayout) findViewById(R.id.home_lowerFrame);
        progressBar = (ProgressBar) findViewById(R.id.home_progressBar);
        searchFragment = new SearchFragment();
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.home_upperFrame, searchFragment);
        transaction.commit();
    }

    @Override
    public void onSearchProcess() {
        if (progressBar.getVisibility() == View.GONE)   {
            lowerFrame.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            lowerFrame.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFragmentInteraction(int[] ids) {
        RadioGroup group = (RadioGroup) findViewById(R.id.search_radioGroup);
        if (ids.length == 0)    {
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
        }
        switch(group.getCheckedRadioButtonId())    {
            case R.id.artist_radioButton:
                createArtistResults(ids);
                break;
        }
    }

    private void createArtistResults(int[] ids) {
        Fragment lowerFrag = getSupportFragmentManager()
                .findFragmentById(R.id.home_lowerFrame);
        ArtistResultsFragment fragment;
        if (lowerFrag != null)  {
            if (lowerFrag instanceof ArtistResultsFragment) {
                fragment = (ArtistResultsFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.home_lowerFrame);
                fragment.setLists(ids);
            } else {
                Bundle bundle = new Bundle();
                bundle.putIntArray(Constants.LIST_KEY, ids);
                fragment = new ArtistResultsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_lowerFrame, fragment)
                        .addToBackStack(null);
                mainLayout.setBackground(getDrawable(R.drawable.sunset_background));
                transaction.commit();
            }
        } else {
            Bundle bundle = new Bundle();
            bundle.putIntArray(Constants.LIST_KEY, ids);
            fragment = new ArtistResultsFragment();
            fragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.home_lowerFrame, fragment)
                    .addToBackStack(null);
            mainLayout.setBackground(getDrawable(R.drawable.sunset_background));
            transaction.commit();
        }
    }

    @Override
    public void onArtistProcess() {
        if (lowerFrame.getVisibility() == View.GONE)    {
            progressBar.setVisibility(View.GONE);
            lowerFrame.setVisibility(View.VISIBLE);
        } else {
            lowerFrame.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onArtistSelected(Artist artist) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.ARTIST_OBJECT_KEY, artist);

        ArtistDisplayFragment fragment = new ArtistDisplayFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_lowerFrame, fragment)
                .addToBackStack(null);
        transaction.commit();
    }
}
