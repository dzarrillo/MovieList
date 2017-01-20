package com.dzartek.movielist.database;

import android.provider.BaseColumns;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class DBModel implements BaseColumns {
    public static final String DATABASE_NAME = "MyMovies.db";

    // Define table favoritemovies
    public static final class FavoriteMovies {
        // Define table name
        public static final String TABLE_MOVIES = "favoritemovies";
        // Define table columns
        public static final String ID = BaseColumns._ID;
        public static final String MOVIEID = "movieid";
        public static final String TITLE = "title";
        public static final String SYNOPSIS = "synopsis";
        public static final String POSTERPATH = "posterpath";
        public static final String RELEASEDATE = "releasdate";
        public static final String VOTEAVERAGE = "voteaverage";
        public static final String POPULARITY = "popularity";
        public static final String BACKDROPPATH = "backdroppath";
        public static final String VOTECOUNT = "votecount";

        // Define projection for favoritemovies table
        public static final String[] MOVIESPROJECTION = new String[] {
                FavoriteMovies.ID,              // 0
                FavoriteMovies.MOVIEID,         // 1
                FavoriteMovies.TITLE,           // 2
                FavoriteMovies.SYNOPSIS,        // 3
                FavoriteMovies.POSTERPATH,      // 4
                FavoriteMovies.RELEASEDATE,     // 5
                FavoriteMovies.VOTEAVERAGE,     // 6
                FavoriteMovies.POPULARITY,      // 7
                FavoriteMovies.BACKDROPPATH,    // 8
                FavoriteMovies.VOTECOUNT       // 9
        };
    }

    public static final class Reviews{
        // Define table name
        public static final String TABLE_REVIEWS = "reviews";
        // Define table columns
        public static final String ID = BaseColumns._ID;
        public static final String AUTHOR = "author";
        public static final String CONTENT = "content";
        public static final String FAVORITEID = "favoriteid";   // Foriegn key

        // Define projection for reviews table
        public static final String[] PROJECTION = new String[] {
                Reviews.AUTHOR,          // 0
                Reviews.CONTENT          // 1
        };

    }

    public static final class Videos{
        // Define table name
        public static final String TABLE_VIDEOS = "videos";
        // Define table columns
        public static final String ID = BaseColumns._ID;
        public static final String TRAILERID = "trailerid";
        public static final String KEY = "key";
        public static final String NAME = "name";
        public static final String SITE = "site";
        public static final String TYPE = "type";
        public static final String VIDEOFAVORITEID = "videofavoriteid";
        // Define projection for reviews table
        public static final String[] PROJECTION = new String[] {
                Videos.KEY,          // 0
                Videos.TRAILERID,    // 1
                Videos.TYPE,         // 2
                Videos.SITE          // 3
        };
    }
}
