package com.dzartek.movielist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dzartek.movielist.datamodel.Movie;
import com.dzartek.movielist.datamodel.MovieConst;

public class MainActivity extends AppCompatActivity implements SortFragmentDialog.OnSortSelectedListener,
        MoviesFragment.OnMovieItemSelected, MovieDetailsFragment.OnMovieRefreshData{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null){
            SharedPreferences mSettings = this.getSharedPreferences("Settings", 0);
            //  If no value found it will default to popularity.desc
            MovieConst.sortOrderBy = mSettings.getString(MovieConst.PREF_SORT_KEY, MovieConst.SORT_HIGHEST_RATED_VALUE); //.SORT_POPULARITY_VALUE);

            Fragment newFragment;

            if (findViewById(R.id.tablet) != null){
                //  Two pane screen
                MovieConst.tablet=true;

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.tablet, new MoviesFragment())
                        .commit();

                newFragment = new MovieDetailsFragment();
                // Populate moviedetails here
                Bundle bundle = new Bundle();
                bundle.putInt("MovieId", 0);
                bundle.putString("Title", "No data");
                bundle.putString("Synopsis", "No data");
                bundle.putString("PosterPath", "No data");
                bundle.putString("ReleaseDate", "No data");
                bundle.putDouble("VoterAverage", 0);
                bundle.putDouble("Popularity", 0);
                bundle.putString("BackDropPath", "No data");
                bundle.putInt("VoteCount", 0);
                newFragment.setArguments(bundle);
            } else {
                //  One pane screen
                MovieConst.tablet=false;
                newFragment = new MoviesFragment();
            }


            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentHolder, newFragment)
                    .commit();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                return true;
            case R.id.action_sort:
                FragmentManager fm = getSupportFragmentManager();
                SortFragmentDialog sortDialog = new SortFragmentDialog();
                sortDialog.show(fm, "Sort-By");
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSortOrderSelected(String sortorder) {
        if(!sortorder.isEmpty()){
            MovieConst.sortOrderBy = sortorder;
            getScreen();
        }
    }
    private void getScreen(){
        int vId;
        if (findViewById(R.id.tablet) != null) {
            //This is a two pane layout (Tablet)
            vId = R.id.tablet;
        } else {
            //One pane layout (cell phone)
            vId = R.id.fragmentHolder;
        }
        //Toast.makeText(this, "getScreen", Toast.LENGTH_SHORT).show();
        MoviesFragment moviemain = (MoviesFragment) getSupportFragmentManager().findFragmentById(vId);
        moviemain.getAPIData();
    }

    @Override
    public void onSelectedMovie(Movie movie) {
        if (movie.getMovieId() > 0){
            MovieDetailsFragment moviedetail = new MovieDetailsFragment();
            //Pass movie data to the movie_detail fragment
            Bundle bundle = new Bundle();
            bundle.putLong("MovieId", movie.getMovieId());
            bundle.putString("Title", movie.getTitle());
            bundle.putString("Synopsis", movie.getSynopsis());
            bundle.putString("PosterPath", movie.getPosterPath());
            bundle.putString("ReleaseDate", movie.getReleaseDate());
            bundle.putDouble("VoterAverage", movie.getVoteAverage());
            bundle.putDouble("Popularity", movie.getPopularity());
            bundle.putString("BackDropPath", movie.getBackDropPath());
            bundle.putLong("VoteCount", movie.getVoteCount());
            bundle.putInt("Id", movie.getId());

            moviedetail.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentHolder, moviedetail)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onNoDataFound() {
        MovieDetailsFragment moviedetail = (MovieDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentHolder);
        moviedetail.clearScreen();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
            if (this.getSupportActionBar() != null){
                this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
    }

    @Override
    public void onRefreshData() {
        if(MovieConst.sortOrderBy.equals(MovieConst.SORT_FAVORITES_VALUE)){
            Toast.makeText(MainActivity.this, "onRefreshdata", Toast.LENGTH_SHORT).show();
            getScreen();
        }
    }
}
