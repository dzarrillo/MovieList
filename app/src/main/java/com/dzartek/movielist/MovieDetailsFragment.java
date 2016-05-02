package com.dzartek.movielist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dzartek.movielist.adapterviews.ReviewAdapter;
import com.dzartek.movielist.adapterviews.TrailerAdapter;
import com.dzartek.movielist.apinetwork.MovieAPI;
import com.dzartek.movielist.database.DatabaseHelper;
import com.dzartek.movielist.datamodel.Movie;
import com.dzartek.movielist.datamodel.MovieConst;
import com.dzartek.movielist.datamodel.MovieReview;
import com.dzartek.movielist.datamodel.MovieTrailer;
import com.dzartek.movielist.datamodel.pojo_moviedetails.Moviedetails;
import com.dzartek.movielist.datamodel.pojo_moviedetails.Result;
import com.dzartek.movielist.datamodel.pojo_moviedetails.Result_;
import com.google.android.youtube.player.YouTubeIntents;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.youtube.player.YouTubeIntents.canResolvePlayVideoIntent;

/**
 * Created by dzarrillo on 4/27/2016.
 */
public class MovieDetailsFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "MovieDetail";
    private TextView tvMovieTitle, tvPopularity, tvVotes, tvReleaseDate, tvSynopsis;
    private ImageView imageViewDetail;
    private ImageButton btnHeart;
    private RatingBar ratingbar;
    private Movie mMovie = new Movie();
    public static ArrayList<MovieTrailer> mTrailerList = new ArrayList<>();
    public static ArrayList<MovieReview> mReveiwList = new ArrayList<>();
    private RecyclerView mTrailerRecyclerview, mReveiwRecyclerview;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReveiwAdapter;
    private RecyclerView.LayoutManager mTrailerLayoutManager, mReveiwLayoutManager;
    private ProgressDialog mProgress;
    private boolean isFavorite = false;
    private DatabaseHelper mdbHelper;

    // Send event back when favorite movie is deleted from database
    public interface OnMovieRefreshData {
        void onRefreshData();
    }

    private OnMovieRefreshData mMovieDataCallback;


    public MovieDetailsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        setHasOptionsMenu(true);

        showHomeButton();

        mMovie.setMovieId(this.getArguments().getLong("MovieId", 0));
        mMovie.setTitle(this.getArguments().getString("Title", "Data not found"));
        mMovie.setSynopsis(this.getArguments().getString("Synopsis", "Data not found"));
        mMovie.setPosterPath(this.getArguments().getString("PosterPath", "Data not found"));
        mMovie.setReleaseDate(this.getArguments().getString("ReleaseDate", "Data not found"));
        mMovie.setVoteAverage(this.getArguments().getDouble("VoterAverage", 0));
        mMovie.setPopularity(this.getArguments().getDouble("Popularity", 0));
        mMovie.setBackDropPath(this.getArguments().getString("BackDropPath", "Data not found"));
        mMovie.setVoteCount(this.getArguments().getLong("VoteCount", 0));
        mMovie.setId(this.getArguments().getInt("Id", 0));
        mProgress = new ProgressDialog(getActivity());
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_moviedetails, container, false);

        initializeWidgets(v);

        initializeTrailerRecycler(v);
        initializeReveiwRecycler(v);
        mProgress.setMessage("Retrieving Movie data! ");
        mProgress.show();
        getDetailData();
        mProgress.dismiss();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        //  Make sure the host implements the method
        try {
            mMovieDataCallback = (OnMovieRefreshData) getActivity();
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString() +
                    " must implement OnMovieRefreshData");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHomeButton();
    }

    private void showHomeButton(){
        //  If tablet(large screen) don't show the home button, smart phone(small screen)
        //  show home button.
        if (MovieConst.tablet) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        if (MovieConst.tablet) {
            menu.findItem(R.id.action_sort).setVisible(true);
        } else {
            menu.findItem(R.id.action_sort).setVisible(false);
        }
        menu.findItem(R.id.action_settings).setVisible(false);
    }


    private void initializeWidgets(View v) {
        String releaseDate = "Release Date: " + mMovie.getReleaseDate();
        float userRating = (float) (mMovie.getVoteAverage() / 2);

        tvMovieTitle = (TextView) v.findViewById(R.id.textViewMovie);
        tvPopularity = (TextView) v.findViewById(R.id.textViewPopularity);
        tvVotes = (TextView) v.findViewById(R.id.textViewVotes);
        ratingbar = (RatingBar) v.findViewById(R.id.ratingBar);
        btnHeart = (ImageButton) v.findViewById(R.id.imageButtonFavorite);
        btnHeart.setOnClickListener(this);

        mdbHelper = new DatabaseHelper(getActivity());
        // SQLite does not have a Long data type.
        isFavorite = mdbHelper.findMovie((int) mMovie.getMovieId());
        if (isFavorite) {
            btnHeart.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            btnHeart.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        tvReleaseDate = (TextView) v.findViewById(R.id.textViewReleaseDate);
        tvSynopsis = (TextView) v.findViewById(R.id.textViewSynopsis);
        imageViewDetail = (ImageView) v.findViewById(R.id.imageViewDetail);

        tvMovieTitle.setText(mMovie.getTitle());
        tvPopularity.setText(new DecimalFormat("#.##").format(mMovie.getPopularity()));
        ratingbar.setRating(userRating);
        tvVotes.setText(new DecimalFormat("#").format(mMovie.getVoteCount()));
        tvReleaseDate.setText(releaseDate);
        tvSynopsis.setText(mMovie.getSynopsis());

        if (screenIsLarge()) {
            Picasso.with(getActivity())
                    .load(mMovie.getBackDropPath())
                    .centerInside()
                    .error(R.drawable.female_film)
                    .resize(300, 500)
                    .into(imageViewDetail);
        } else {
            Picasso.with(getActivity())
                    .load(mMovie.getBackDropPath())
                    .centerInside()
                    .error(R.drawable.female_film)
                    .resize(500, 700)
                    .into(imageViewDetail);
        }

    }

    private void initializeReveiwRecycler(View v) {
        mReveiwAdapter = new ReviewAdapter(getActivity(), mReveiwList);  // 1
        mReveiwLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);  // 2
        mReveiwRecyclerview = (RecyclerView) v.findViewById(R.id.reveiwRecyclerList);  // 3
        mReveiwRecyclerview.setHasFixedSize(true);  // 4
        mReveiwRecyclerview.setLayoutManager(mReveiwLayoutManager);  // 5
        mReveiwRecyclerview.setAdapter(mReveiwAdapter);  // 6
    }

    private void initializeTrailerRecycler(View v) {
        mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailerList);  // 1
        mTrailerLayoutManager = new GridLayoutManager(getActivity(), 3);  // 2
        mTrailerRecyclerview = (RecyclerView) v.findViewById(R.id.videoRecyclerList);  // 3
        mTrailerRecyclerview.setHasFixedSize(true);  // 4
        mTrailerRecyclerview.setLayoutManager(mTrailerLayoutManager);  // 5
        mTrailerRecyclerview.setAdapter(mTrailerAdapter);  // 6

        mTrailerRecyclerview.addOnItemTouchListener(new TrailerTouchListener(getActivity(),
                mTrailerRecyclerview, new MoviesFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
            }

            @Override
            public void onLongClick(View view, int position) {
                // Use the canResolvePlayVideoIntent(Context context)
                if (canResolvePlayVideoIntent(getActivity())) {
                    Intent intent = YouTubeIntents.createPlayVideoIntentWithOptions(getActivity(), mTrailerList.get(position).getKey(), false, true);
                    startActivity(intent);
                } else {
                    showMyToast("Youtube application needs to be installed!");
                }
            }
        }));
    }

    private void getDetailData() {
        mReveiwList.clear();
        mTrailerList.clear();

        if (!(MovieConst.sortOrderBy.equals(MovieConst.SORT_FAVORITES_VALUE))) {       //!mSortFavorite) {
            MovieAPI.Factory.getInstance().getMovieDetails((int) mMovie.getMovieId(),
                    MovieConst.API_MOVIE_KEY,
                    "credits,releases,videos,reviews").enqueue(new Callback<Moviedetails>() {
                @Override
                public void onResponse(Call<Moviedetails> call, Response<Moviedetails> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getReviews().getTotalResults() > 0) {

                            for (Result_ result : response.body().getReviews().getResults()) {
                                MovieReview review = new MovieReview();
                                review.setAuthor(result.getAuthor().trim());
                                review.setContent(result.getContent().trim());
                                mReveiwList.add(review);
                            }
                        }
                        if (!response.body().getVideos().getResults().isEmpty()) {

                            for (Result result : response.body().getVideos().getResults()) {
                                MovieTrailer trailer = new MovieTrailer();
                                trailer.setTrailerId(result.getId());
                                trailer.setKey(result.getKey());
                                trailer.setName(result.getName());
                                trailer.setSite(result.getSite());
                                trailer.setType(result.getType());
                                mTrailerList.add(trailer);
                            }
                        }

                        //updateDetailDisplay();
                        mReveiwAdapter.notifyDataSetChanged();
                        mTrailerAdapter.notifyDataSetChanged();

                    } else {
                        ResponseBody errBody = response.errorBody();
                        Log.d(TAG, errBody.toString());
                    }
                }

                @Override
                public void onFailure(Call<Moviedetails> call, Throwable t) {
                    Log.e(TAG, "Failed: " + t.getMessage());
                    showMyToast("Failed to get Data!");
                }


            });
        } else {
            // Get favorites from database
            mdbHelper = new DatabaseHelper(getActivity());
            mReveiwList = mdbHelper.getReviews(mMovie.getId());
            mTrailerList = mdbHelper.getVideos(mMovie.getId());

            mReveiwAdapter = new ReviewAdapter(getActivity(), mReveiwList);
            mReveiwLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mReveiwRecyclerview.setHasFixedSize(true);
            mReveiwRecyclerview.setLayoutManager(mReveiwLayoutManager);
            mReveiwRecyclerview.setAdapter(mReveiwAdapter);

            mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailerList);
            mTrailerLayoutManager = new GridLayoutManager(getActivity(), 3);
            mTrailerRecyclerview.setHasFixedSize(true);
            mTrailerRecyclerview.setLayoutManager(mTrailerLayoutManager);
            mTrailerRecyclerview.setAdapter(mTrailerAdapter);
        }
    }

    @Override
    public void onClick(View v) {
        // This is for when you want to add a movie to favorites by clicking on heart
        switch (v.getId()) {
            case R.id.imageButtonFavorite:
                if(mMovie != null){
                    if (mMovie.getMovieId() > 0) {
                        mdbHelper = new DatabaseHelper(getActivity());
                        if (isFavorite) {
                            // Delete from database
                            int rowid = mdbHelper.deleteMovie((int) mMovie.getMovieId());
                            if (rowid > 0) {
                                btnHeart.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                isFavorite = false;

                                if (MovieConst.tablet) {
                                    // Refresh screen with updated data
                                    MovieConst.refreshData = true;
                                    MovieConst.refreshDBData = true;
                                    mMovieDataCallback.onRefreshData();
                                }
                            } else {
                                showMyToast("Movie was not removed from favorites!");
                            }
                        } else {
                            //insert into database
                            long rowid = mdbHelper.insertFavoriteMovies(mMovie, mReveiwList, mTrailerList);

                            if (rowid > 0) {
                                btnHeart.setImageResource(R.drawable.ic_favorite_black_24dp);
                                isFavorite = true;
                                showMyToast("Movie added to favorites!");
                                if (MovieConst.tablet) {
                                    // Refresh screen with updated data
                                    MovieConst.refreshData = true;
                                    MovieConst.refreshDBData = true;
                                    mMovieDataCallback.onRefreshData();
                                }

                            } else {
                                showMyToast("Movie was not added to favorites!");
                            }
                        }
                    }
                }

                break;
        }
    }

    static class TrailerTouchListener implements RecyclerView.OnItemTouchListener {
        private final String TAG = "TrailerTouchListener";
        private GestureDetector gestureDetector;
        private MoviesFragment.ClickListener clickListener;

        public TrailerTouchListener(Context context, final RecyclerView recyclerView, final MoviesFragment.ClickListener clickListener) {
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
            Log.d(TAG, "onInterceptTouchEvent! " + gestureDetector.onTouchEvent(e));
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    /* ToDo:  Check to see if this actually works */
    private boolean screenIsLarge() {
        int screenMask = getResources().getConfiguration().screenLayout;

        if ((screenMask & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return true;
        }

        if ((screenMask & Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            return true;
        }
        return false;
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

    public void clearScreen(){
        btnHeart.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        isFavorite = false;

        ratingbar.setRating(0);
        tvMovieTitle.setText("");
        tvPopularity.setText("");
        tvVotes.setText("");
        tvReleaseDate.setText("");
        tvSynopsis.setText("");

        imageViewDetail.setImageResource(R.drawable.female_film);
        mMovie = null;
        mReveiwList.clear();
        mReveiwAdapter = new ReviewAdapter(getActivity(), mReveiwList);
        mReveiwLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mReveiwRecyclerview.setHasFixedSize(true);
        mReveiwRecyclerview.setLayoutManager(mReveiwLayoutManager);
        mReveiwRecyclerview.setAdapter(mReveiwAdapter);
        mTrailerList.clear();
        mTrailerAdapter = new TrailerAdapter(getActivity(), mTrailerList);
        mTrailerLayoutManager = new GridLayoutManager(getActivity(), 3);
        mTrailerRecyclerview.setHasFixedSize(true);
        mTrailerRecyclerview.setLayoutManager(mTrailerLayoutManager);
        mTrailerRecyclerview.setAdapter(mTrailerAdapter);

    }
}
