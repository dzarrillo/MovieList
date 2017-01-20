package com.dzartek.movielist.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dzarrillo on 5/5/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.dzartek.movielist.Provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Map the tables
    public static final String PATH_FAVORITMOVIES = "favoritemovies";
    public static final String PATH_REVIEWS = "reviews";
    public static final String PATH_VIDEOS = "videos";

    // INNER CLASS THAT DEFINES THE TABLE CONTENTS OF FAVORITEMOVIES
    public static final class FavoriteMoviesEntry implements BaseColumns{
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITMOVIES).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITMOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITMOVIES;

        // Table name
        public static final String TABLE_NAME = "favoritemovies";

        public static final String COLUMN_MOVIEID_SETTING = "movie_setting";
    }
}
