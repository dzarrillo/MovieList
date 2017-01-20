package com.dzartek.movielist.datamodel;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class MovieConst {
    public static final String API_MOVIE_KEY = "a89bbf6a5b86d6775463b2524101bdab";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    public static final String BASE_YOUTUBE_URL = "http://img.youtube.com/vi/";
    //  These are the possible sorting values can do
    public static final String SORT_POPULARITY_VALUE = "popularity.desc";
    public static final String SORT_HIGHEST_RATED_VALUE = "vote_average.desc";
    public static final String SORT_FAVORITES_VALUE = "FAVORITES";  // SQLite database

    //  Make sure movies have at least a certain amount of votes in order to be selected
    public static final String VOTE_COUNT = "vote_count.desc";

    //  This is the Sharedpreferences sort key
    public static final String PREF_SORT_KEY = "SORT_BY";

    //  This will hold the sorting values: SORT_POPULARITY_VALUE, SORT_HIGHEST_RATED_VALUE, SORT_FAVORITES_VALUE
    public static String sortOrderBy;
    // Used to inform the recyclerviewadapter(Movies) the database dataset has changed
    public static boolean refreshDBData = false;
    public static boolean tablet = false;
    public static boolean refreshData = false;


}
