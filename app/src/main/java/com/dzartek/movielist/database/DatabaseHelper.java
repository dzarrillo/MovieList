package com.dzartek.movielist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dzartek.movielist.datamodel.Movie;
import com.dzartek.movielist.datamodel.MovieReview;
import com.dzartek.movielist.datamodel.MovieTrailer;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = DatabaseHelper.class.getSimpleName();
    // Database name
    private final static String DB_NAME =DBModel.DATABASE_NAME;
    // Database version
    private final static int DB_VERSION = 1;

    // favoritemovies table
    private final static String FAVORITEMOVIES_TABLE_NAME = DBModel.FavoriteMovies.TABLE_MOVIES;
    private final static String FAVORITEMOVIES_ROW_ID = DBModel.FavoriteMovies.ID;
    private final static String FAVORITEMOVIES_ROW_MOVIEID = DBModel.FavoriteMovies.MOVIEID;
    private final static String FAVORITEMOVIES_ROW_TITLE = DBModel.FavoriteMovies.TITLE;
    private final static String FAVORITEMOVIES_ROW_SYNOPSIS = DBModel.FavoriteMovies.SYNOPSIS;
    private final static String FAVORITEMOVIES_ROW_POSTERPATH=DBModel.FavoriteMovies.POSTERPATH;
    private final static String FAVORITEMOVIES_ROW_RELEASEDATE=DBModel.FavoriteMovies.RELEASEDATE;
    private final static String FAVORITEMOVIES_ROW_VOTEAVERAGE=DBModel.FavoriteMovies.VOTEAVERAGE;
    private final static String FAVORITEMOVIES_ROW_POPULARITY=DBModel.FavoriteMovies.POPULARITY;
    private final static String FAVORITEMOVIES_ROW_BACKDROPPATH=DBModel.FavoriteMovies.BACKDROPPATH;
    private final static String FAVORITEMOVIES_ROW_VOTECOUNT=DBModel.FavoriteMovies.VOTECOUNT;
    // reviews table
    private final static String REVIEWS_TABLE_NAME = DBModel.Reviews.TABLE_REVIEWS;
    private final static String REVIEWS_ROW_ID = DBModel.Reviews.ID;
    private final static String REVIEWS_ROW_FAVORITEID = DBModel.Reviews.FAVORITEID;
    private final static String REVIEWS_ROW_AUTHOR = DBModel.Reviews.AUTHOR;
    private final static String REVIEWS_ROW_CONTENT = DBModel.Reviews.CONTENT;
    // videos table
    private final static String VIDEOS_TABLE_NAME = DBModel.Videos.TABLE_VIDEOS;
    private final static String VIDEOS_ROW_ID = DBModel.Videos.ID;
    private final static String VIDEOS_ROW_FAVORITEID = DBModel.Videos.VIDEOFAVORITEID;
    private final static String VIDEOS_ROW_TRAILERID = DBModel.Videos.TRAILERID;
    private final static String VIDEOS_ROW_KEY = DBModel.Videos.KEY;
    private final static String VIDEOS_ROW_NAME = DBModel.Videos.NAME;
    private final static String VIDEOS_ROW_SITE = DBModel.Videos.SITE;
    private final static String VIDEOS_ROW_TYPE = DBModel.Videos.TYPE;


    // SQL statement to create the favoritemovies table
    private final static String FAVORITESMOVIES_TABLE_CREATE =
            "CREATE TABLE " +
                    FAVORITEMOVIES_TABLE_NAME + " (" +
                    FAVORITEMOVIES_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    FAVORITEMOVIES_ROW_MOVIEID + " INTEGER, " +
                    FAVORITEMOVIES_ROW_TITLE + " TEXT, " +
                    FAVORITEMOVIES_ROW_SYNOPSIS + " TEXT, " +
                    FAVORITEMOVIES_ROW_POSTERPATH+ " TEXT, " +
                    FAVORITEMOVIES_ROW_RELEASEDATE + " TEXT, " +
                    FAVORITEMOVIES_ROW_VOTEAVERAGE + " REAL, " +
                    FAVORITEMOVIES_ROW_POPULARITY + " REAL, " +
                    FAVORITEMOVIES_ROW_BACKDROPPATH + " TEXT, " +
                    FAVORITEMOVIES_ROW_VOTECOUNT + " REAL" + ");";

    // SQL statement to create the reviews table
    private final static String REVIEWS_TABLE_CREATE  =
            "CREATE TABLE " +
                    REVIEWS_TABLE_NAME + " (" +
                    REVIEWS_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    REVIEWS_ROW_AUTHOR + " TEXT, " +
                    REVIEWS_ROW_CONTENT + " TEXT, " +
                    REVIEWS_ROW_FAVORITEID + " INTEGER, " +
                    "foreign key(" + REVIEWS_ROW_FAVORITEID + ") references " +
                    FAVORITEMOVIES_TABLE_NAME + "(" + FAVORITEMOVIES_ROW_ID + ")" +
                    " ON DELETE CASCADE);";

    // SQL statement to create the reviews table
    private final static String VIDEOS_TABLE_CREATE  =
            "CREATE TABLE " +
                    VIDEOS_TABLE_NAME + " (" +
                    VIDEOS_ROW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    VIDEOS_ROW_TRAILERID + " INTEGER, " +
                    VIDEOS_ROW_KEY + " TEXT, " +
                    VIDEOS_ROW_NAME + " TEXT, " +
                    VIDEOS_ROW_SITE + " TEXT, " +
                    VIDEOS_ROW_TYPE + " TEXT, " +
                    VIDEOS_ROW_FAVORITEID + " INTEGER, " +
                    "foreign key(" + VIDEOS_ROW_FAVORITEID + ") references " +
                    FAVORITEMOVIES_TABLE_NAME + "(" + FAVORITEMOVIES_ROW_ID + ")" +
                    " ON DELETE CASCADE);";

    public DatabaseHelper(Context context) {
        super(context, DBModel.DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create FAVORITESMOVIES table
        db.execSQL(FAVORITESMOVIES_TABLE_CREATE);
        db.execSQL(REVIEWS_TABLE_CREATE);
        db.execSQL(VIDEOS_TABLE_CREATE);
        Log.i(TAG, "Creating tables with query!");

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // By default foreign_key=off - cascading deletes will not work
        db.execSQL("PRAGMA foreign_keys=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITEMOVIES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + REVIEWS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + VIDEOS_TABLE_NAME);
        onCreate(db);
    }

    public long insertFavoriteMovies(Movie movie, ArrayList<MovieReview> reviewList, ArrayList<MovieTrailer> videoList){
        long rowId = 0;
        Log.d(TAG, "in favoritemovies record!");

        // Get reference to writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //Create contentvalues to add database
        ContentValues contentFavoritevalues = new ContentValues();
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_MOVIEID, movie.getMovieId());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_TITLE, movie.getTitle());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_SYNOPSIS, movie.getSynopsis());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_POSTERPATH, movie.getPosterPath());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_RELEASEDATE, movie.getReleaseDate());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_VOTEAVERAGE, movie.getVoteAverage());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_POPULARITY, movie.getPopularity());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_BACKDROPPATH, movie.getBackDropPath());
        contentFavoritevalues.put(FAVORITEMOVIES_ROW_VOTECOUNT, movie.getVoteCount());

        db.beginTransaction();
        try {
            // Insert data to table, db.insert returns a long
            rowId = db.insert(FAVORITEMOVIES_TABLE_NAME, null, contentFavoritevalues);

            // Create contentReviewvalues
            ContentValues contentReviewvalues = new ContentValues();
            contentReviewvalues.put(REVIEWS_ROW_FAVORITEID, rowId);
            int count = reviewList.size();
            for(int i = 0; i < count; i++){
                contentReviewvalues.put(REVIEWS_ROW_AUTHOR, reviewList.get(i).getAuthor());
                contentReviewvalues.put(REVIEWS_ROW_CONTENT, reviewList.get(i).getContent());
                db.insert(REVIEWS_TABLE_NAME, null, contentReviewvalues);
            }

            count = videoList.size();
            for(int i = 0; i < count; i++){
                ContentValues contentVideovalues = new ContentValues();
                contentVideovalues.put(VIDEOS_ROW_FAVORITEID, rowId);
                contentVideovalues.put(VIDEOS_ROW_KEY, videoList.get(i).getKey());
                contentVideovalues.put(VIDEOS_ROW_TRAILERID, videoList.get(i).getTrailerId());
                contentVideovalues.put(VIDEOS_ROW_NAME, videoList.get(i).getName());
                contentVideovalues.put(VIDEOS_ROW_SITE, videoList.get(i).getSite());
                contentVideovalues.put(VIDEOS_ROW_TYPE, videoList.get(i).getType());

                db.insert(VIDEOS_TABLE_NAME, null, contentVideovalues);
            }

            db.setTransactionSuccessful();
            Log.d(TAG, "favoritemovies record added!");
        } catch (Exception e) {
            rowId=0;
            Log.d(TAG, "Data not inserted into database");
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        // Close database
        db.close();
        return rowId;
    }

    public boolean findMovie(int movieId){
        boolean found = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] columns = {"title", "movieid"};
        Cursor cursor = db.query(FAVORITEMOVIES_TABLE_NAME, columns, FAVORITEMOVIES_ROW_MOVIEID + "=?",
                new String[] { Integer.toString(movieId) } , null, null,null);
        while (cursor.moveToNext()){
            Log.d(TAG, "Title: " + cursor.getString(0) + " movieid: " + cursor.getInt(1));
            found=true;
        }
        cursor.close();
        db.close();
        return found;
    }

    public int deleteMovie(int movieId){
        int count;
        SQLiteDatabase db = this.getWritableDatabase();

        count = db.delete(FAVORITEMOVIES_TABLE_NAME, FAVORITEMOVIES_ROW_MOVIEID + "=?",
                new String[] {Integer.toString(movieId)});

        return count;
    }

    public ArrayList<Movie> getFavoriteMovies(){
        ArrayList<Movie> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(FAVORITEMOVIES_TABLE_NAME,
                DBModel.FavoriteMovies.MOVIESPROJECTION,
                null, null, null, null, null);

        if (cursor.getCount() > 0){
            while (cursor.moveToNext()){
                //Log.d(TAG, "Title: " + cursor.getString(0) + " movieid: " + cursor.getLong(1));
                Movie movie = new Movie();
                movie.setId(cursor.getInt(0));
                movie.setMovieId(cursor.getInt(1));
                movie.setTitle(cursor.getString(2));
                movie.setSynopsis(cursor.getString(3));
                movie.setPosterPath(cursor.getString(4));
                movie.setReleaseDate(cursor.getString(5));
                movie.setVoteAverage(cursor.getDouble(6));
                movie.setPopularity(cursor.getDouble(7));
                movie.setBackDropPath(cursor.getString(8));
                movie.setVoteCount(cursor.getInt(9));
                movieList.add(movie);
            }
        } else {
//            Movie movie = new Movie();
//            movie.setId(0);
//            movie.setMovieId(0);
//            movie.setTitle("n/a");
//            movie.setSynopsis("n/a");
//            movie.setPosterPath("n/a");
//            movie.setReleaseDate("n/a");
//            movie.setVoteAverage(0);
//            movie.setPopularity(0);
//            movie.setBackDropPath("n/a");
//            movie.setVoteCount(0);
//            movieList.add(movie);
        }

        cursor.close();
        db.close();
        return movieList;
    }

    public ArrayList<MovieReview> getReviews(int id){
        ArrayList<MovieReview> reviewList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(REVIEWS_TABLE_NAME, DBModel.Reviews.PROJECTION, REVIEWS_ROW_FAVORITEID + "=?",
                new String[]{Integer.toString(id)}, null, null, null);
        while (cursor.moveToNext()){
            MovieReview review = new MovieReview();
            review.setAuthor(cursor.getString(0));
            review.setContent(cursor.getString(1));
            reviewList.add(review);
        }
        cursor.close();
        db.close();
        return reviewList;
    }

    public ArrayList<MovieTrailer> getVideos(int id){
        ArrayList<MovieTrailer> videoList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(VIDEOS_TABLE_NAME, DBModel.Videos.PROJECTION, VIDEOS_ROW_FAVORITEID + "=?",
                new String[]{Integer.toString(id)}, null, null, null);
        while (cursor.moveToNext()){
            MovieTrailer video = new MovieTrailer();
            video.setKey(cursor.getString(0));
            video.setTrailerId(cursor.getString(1));
            video.setType(cursor.getString(2));
            video.setSite(cursor.getString(3));
            videoList.add(video);
        }
        cursor.close();
        db.close();
        return videoList;
    }
}
