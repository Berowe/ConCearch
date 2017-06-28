package com.rowenetworks.concearch.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rowenetworks.concearch.tools.Constants;
import com.rowenetworks.concearch.tools.Database;
import com.rowenetworks.concearch.R;
import com.rowenetworks.concearch.fragments.ArtistDisplayFragment;
import com.rowenetworks.concearch.fragments.ArtistResultsFragment;
import com.rowenetworks.concearch.fragments.ConcertDisplayFragment;
import com.rowenetworks.concearch.fragments.SearchFragment;
import com.rowenetworks.concearch.fragments.VenueDisplayFragment;
import com.rowenetworks.concearch.fragments.VenueResultsFragment;
import com.rowenetworks.concearch.model.Artist;
import com.rowenetworks.concearch.model.Concert;
import com.rowenetworks.concearch.model.Venue;
/**
 * @author Braxton Rowe
 * @version 1.1
 * The MainActivity class is the container of fragments and the hub of their interactions.
 */
public class MainActivity extends AppCompatActivity implements
        SearchFragment.OnSearchFragmentInteractionListener,
        ArtistResultsFragment.OnArtistSelectedListener,
        ArtistDisplayFragment.OnConcertSelectedListener,
        VenueResultsFragment.OnVenueSelected,
        VenueDisplayFragment.OnVenueConcertSelected {

    SearchFragment searchFragment;
    EditText searchEditText;
    RadioButton artistRadioButton;
    RadioButton venueRadioButton;
    LinearLayout mainLayout;
    FrameLayout lowerFrame;
    ProgressBar progressBar;
    Boolean artistSelectedFromList;

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


    /**
     * This method is called from the TextView in the XML layout for the SimpleListAdapter.
     * When a user clicks an artist, either from the concert performer list, or similar artist
     * list, this method will return the user to the search screen and search for the artist.
     * @param view The TextView from holder_simple_list.xml
     */
    public void listArtistClicked(View view)    {
        lowerFrame.setVisibility(View.GONE);
        artistSelectedFromList = true;
        searchEditText = (EditText) findViewById(R.id.search_editText);
        artistRadioButton = (RadioButton) findViewById(R.id.artist_radioButton);
        venueRadioButton = (RadioButton) findViewById(R.id.venue_radioButton);
        TextView sim_text = (TextView) view.findViewById(R.id.simple_list_textView);
        String artistName = "";
        if (sim_text != null)    {
            artistName = sim_text.getText().toString();
        }
        searchFragment.onArtistSelected(artistName);
    }

    /**
     * This is a method from the SearchFragmentInteractionListener.  It is called when a user
     * clicks the search button, or chooses to get more detailed information.  It is called from
     * the Tasks in the tasks package during the onPreExecute and onPostExecute methods within the
     * ASyncTasks.  It hides whatever fragment is in the lower frame and shows the progress bar, or
     * hides the progress bar and shows the new fragment in the lower frame.
     */
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

    /**
     * Same notes as onSearchProcess.
     */
    @Override
    public void onArtistProcess() { onSearchProcess(); }

    /**
     * Same notes as onSearchProcess.
     */
    @Override
    public void onVenueProcess() { onSearchProcess(); }

    /**
     * This is a method from the SearchFragmentInteractionListener.  It is called during either the
     * ArtistSearchTask or VenueSearchTask onPostExecute methods.  It checks to see which
     * RadioButton is selected and calls the appropriate method.
     * @param ids The IDs of the objects in the Database class related to the user's search.
     */
    @Override
    public void onFragmentInteraction(int[] ids) {
        RadioGroup group = (RadioGroup) findViewById(R.id.search_radioGroup);
        switch(group.getCheckedRadioButtonId())    {
            case R.id.artist_radioButton:
                createArtistResults(ids);
                break;
            case R.id.venue_radioButton:
                createVenueResults(ids);
                break;
        }
    }

    /**
     * This helper method is called from the onFragmentInteraction method to show the fragment
     * showing the artist results from the user's search.  If no IDs were sent from the
     * ArtistSearchTask, then the program shows a Toast with message stating no results found.
     * @param ids The IDs of the objects in the Database class related to the user's search.
     */
    private void createArtistResults(int[] ids) {
        if (ids.length == 0) {
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
            lowerFrame.setVisibility(View.GONE);
            searchFragment.onClick(searchFragment.getView());
        } else {
            lowerFrame.setVisibility(View.VISIBLE);
            Fragment lowerFrag = getSupportFragmentManager()
                    .findFragmentById(R.id.home_lowerFrame);
            ArtistResultsFragment fragment;
            if (lowerFrag != null) {
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
    }

    /**
     * This helper method is called from the onFragmentInteraction method to show the fragment
     * showing the venue results from the user's search.  If no IDs were sent from the
     * VenueSearchTask, then the program shows a Toast with message stating no results found.
     * @param ids The IDs of the objects in the Database class related to the user's search.
     */
    private void createVenueResults(int[] ids)  {
        if (ids.length == 0)    {
            Toast.makeText(this, R.string.no_results, Toast.LENGTH_SHORT).show();
            lowerFrame.setVisibility(View.GONE);
            searchFragment.onClick(searchFragment.getView());
        } else {
            lowerFrame.setVisibility(View.VISIBLE);
            Fragment lowerFrag = getSupportFragmentManager()
                    .findFragmentById(R.id.home_lowerFrame);
            VenueResultsFragment fragment;
            if (lowerFrag != null)  {
                if (lowerFrag instanceof VenueResultsFragment)  {
                    fragment = (VenueResultsFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.home_lowerFrame);
                    fragment.updateList(ids);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putIntArray(Constants.LIST_KEY, ids);
                    fragment = new VenueResultsFragment();
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
                fragment = new VenueResultsFragment();
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.home_lowerFrame, fragment)
                        .addToBackStack(null);
                mainLayout.setBackground(getDrawable(R.drawable.sunset_background));
                transaction.commit();
            }
        }
    }

    /**
     * This method is from the OnConcertSelected interface found in the ConcertListAdapter.  It
     * shows a fragment with information about the selected concert.
     * @param concert The concert the user selected.
     */
    @Override
    public void onConcertSelected(Concert concert) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CONCERT_OBJECT_KEY, concert);
        ConcertDisplayFragment fragment = new ConcertDisplayFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_lowerFrame, fragment)
                .addToBackStack(null);
        transaction.commit();
    }

    /**
     * Same notes as onConcertSelected.
     * @param concert The concert the user selected.
     */
    @Override
    public void onVenueConcertSelected(Concert concert) {
        onConcertSelected(concert);
    }

    /**
     * This method is from the OnArtistSelected interface from the ArtistResultsFragment.  Once a
     * user selects an artist from the artist search results, it starts an ArtistDetailsTask.  That
     * task calls this method passing the artist object.
     * @param artist The artist that the user selected.
     */
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

    /**
     * This method is from the OnVenueSelected interface from the VenueResultsFragment.  Once a
     * user selects a venue from the venue search results, it starts a VenueDetailsTask.  That
     * task calls this method passing the venue object.
     * @param venue The venue that the user selected.
     */
    @Override
    public void onVenueSelected(Venue venue) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.VENUE_OBJECT_KEY, venue);
        VenueDisplayFragment fragment = new VenueDisplayFragment();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_lowerFrame, fragment)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed()  {
        super.onBackPressed();
        Fragment fragment = getSupportFragmentManager()
                .findFragmentById(R.id.home_lowerFrame);
        if (fragment == null)   {
            searchFragment.onClick(searchFragment.getView());
        }
        if (artistSelectedFromList != null) {
            if (artistSelectedFromList) {
                lowerFrame.setVisibility(View.VISIBLE);
                artistSelectedFromList = false;
            }
        }
    }
}
