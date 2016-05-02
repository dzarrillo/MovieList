package com.dzartek.movielist;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dzartek.movielist.adapterviews.MovieAdapter;
import com.dzartek.movielist.apinetwork.MovieAPI;
import com.dzartek.movielist.database.DatabaseHelper;
import com.dzartek.movielist.datamodel.Movie;
import com.dzartek.movielist.datamodel.MovieConst;
import com.dzartek.movielist.datamodel.pojo_movies.Movies;
import com.dzartek.movielist.datamodel.pojo_movies.Result;

import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dzarrillo on 4/24/2016.
 */
public class MoviesFragment extends Fragment {
    private final String TAG = "MainActivity";
    private static final String MOVIES_STATE = "state_movies";
    private ArrayList<Movie> mMovieList = new ArrayList<>();
    private ProgressDialog mProgress;
    private RecyclerView mRecyclerviewMovies;
    private MovieAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean screen_Rotated = false;
    private static final String SCREENROTATED = "screenrotated";

    //  will send the selected movie back to mainactivity
    public interface OnMovieItemSelected {
        void onSelectedMovie(Movie movie);
        void onNoDataFound();
    }

    private OnMovieItemSelected mMovieItemCallback;


    public MoviesFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retain the the view state and all it's data.
        setRetainInstance(true);
        setHasOptionsMenu(true);

        mProgress = new ProgressDialog(getActivity());
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movies, container, false);


        if(!isOnLine()){
            showMyToast("Network is not available,"
                    + "\nfetching favorites from database \n if available!");
            MovieConst.sortOrderBy = MovieConst.SORT_FAVORITES_VALUE;
        }


        if ((!screen_Rotated) && (MovieConst.tablet)){
            mProgress.setMessage("Retrieving Movie data! ");
            mProgress.show();
            getAPIData();
            mProgress.dismiss();
        }

        if (!MovieConst.tablet){
            mProgress.setMessage("Retrieving Movie data! ");
            mProgress.show();
            getAPIData();
            mProgress.dismiss();
        }

        initializeRecyclerView(v);

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_sort).setVisible(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        //  Make sure the host implements the methods
        try {
            mMovieItemCallback = (OnMovieItemSelected) getActivity();
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnMovieItemSelected");
        }

        //  When in two pane screen initially send data to the second pane
        if (MovieConst.sortOrderBy.equals(MovieConst.SORT_FAVORITES_VALUE)) {
            if ((MovieConst.tablet) && (!screen_Rotated)) {
                if (mMovieList.size() > 0) {
                    mMovieItemCallback.onSelectedMovie(mMovieList.get(0));
                }
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        screen_Rotated = true;
    }

    private void initializeRecyclerView(View v) {
        Log.d(TAG, "initializeRecyclerView");
        mAdapter = new MovieAdapter(getActivity(), mMovieList);
        mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerviewMovies = (RecyclerView) v.findViewById(R.id.recyclerViewMovies);
        mRecyclerviewMovies.setHasFixedSize(true);
        mRecyclerviewMovies.setLayoutManager(mLayoutManager);
        mRecyclerviewMovies.setAdapter(mAdapter);

        mRecyclerviewMovies.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), mRecyclerviewMovies,
                new ClickListener() {

                    @Override
                    public void onClick(View view, int position) {
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        mMovieItemCallback.onSelectedMovie(mMovieList.get(position));
                    }
                }));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SCREENROTATED, true);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            screen_Rotated = savedInstanceState.getBoolean(SCREENROTATED);
        }
    }

    protected boolean isOnLine() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    protected void getAPIData() {
        // if(!screen_rotation){
        if (MovieConst.sortOrderBy.equals(MovieConst.SORT_FAVORITES_VALUE)) {

            Log.d(TAG, "getAPIdata - favorites");
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
            //  used to inform that it is an existing dataset
            if (mMovieList.size() > 1) {
                mMovieList.clear();
            }
            //Get data from database - this has to be asynchronous call
            mMovieList = dbHelper.getFavoriteMovies();
            if (MovieConst.refreshDBData) {

                //mAdapter.notifyDataSetChanged();

                /* Reviewer:  The notifyDataSetChanged() was not working when I get the data from the
                    database, so I had to do the below code.  I'm hoping once I learn about contentproviders
                    & the loadmanager/loadcursor that data change notifications become seamless between
                    the API calls & database calls.
                    Any recomendations would be great.......
                 */

                mAdapter = new MovieAdapter(getActivity(), mMovieList);
                mLayoutManager = new GridLayoutManager(getActivity(), 3);
                mRecyclerviewMovies.setHasFixedSize(true);
                mRecyclerviewMovies.setLayoutManager(mLayoutManager);
                mRecyclerviewMovies.setAdapter(mAdapter);

                if (MovieConst.tablet) {
                    if (mMovieList.size() > 0) {
                        mMovieItemCallback.onSelectedMovie(mMovieList.get(0));
                    } else{
                        mMovieItemCallback.onNoDataFound();
                    }
                }

                MovieConst.refreshDBData = false;
            }
            //mAdapter.notifyDataSetChanged();
            if (mMovieList.size() < 1) {
                showMyToast("Favorites not found in database!");
            }

        } else {

            MovieAPI.Factory.getInstance().getMovies(MovieConst.sortOrderBy, MovieConst.API_MOVIE_KEY).enqueue(new Callback<Movies>() {
                @Override
                public void onResponse(Call<Movies> call, Response<Movies> response) {
                    if (response.isSuccessful()) {
                        mMovieList.clear();
                        for (Result result : response.body().getResults()) {
                            Movie movie = new Movie();
                            movie.setMovieId(result.getId());
                            movie.setTitle(result.getOriginalTitle());
                            movie.setSynopsis(result.getOverview());
                            movie.setPosterPath(MovieConst.BASE_IMAGE_URL + result.getPosterPath() + "&api_key=" + MovieConst.API_MOVIE_KEY);
                            movie.setReleaseDate(result.getReleaseDate());
                            movie.setVoteAverage(result.getVoteAverage());
                            movie.setPopularity(result.getPopularity());
                            movie.setMovieId(result.getId());
                            movie.setBackDropPath(MovieConst.BASE_IMAGE_URL + result.getBackdropPath() + "&api_key=" + MovieConst.API_MOVIE_KEY);
                            movie.setVoteCount(result.getVoteCount());
                            mMovieList.add(movie);
                        }

                        mAdapter.notifyDataSetChanged();
                        if (MovieConst.tablet) {
                            mMovieItemCallback.onSelectedMovie(mMovieList.get(0));
                        }
                    } else {
                        ResponseBody errBody = response.errorBody();
                        Log.d(TAG, errBody.toString());
                        showMyToast(errBody.toString());
                    }
                }

                @Override
                public void onFailure(Call<Movies> call, Throwable t) {
                    showMyToast("Failed to get Data!");
                }
            });
        }

    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private final String TAG = "RecyclerTouchListener";
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            Log.d(TAG, "constructor invoked!");
            this.clickListener = clickListener;

            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    Log.d(TAG, "onLongPress! " + e);
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onLongClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d(TAG, "onTouchEvent!");
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public void showMyToast(String msg) {
        TextView tv = new TextView(getActivity());
        tv.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.lightgrey));
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.primarytext));
        tv.setPadding(25, 25, 25, 25);
        tv.setTextSize(19);
        tv.setTypeface(null, Typeface.NORMAL);
        tv.setText(msg);

        Toast tt = new Toast(getActivity());
        tt.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 150);
        tt.setView(tv);
        tt.show();
    }
}
