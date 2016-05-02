package com.dzartek.movielist.datamodel;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class Movie implements Parcelable{

    private int id;
    private long movieId;
    private String title;
    private String synopsis;
    private String posterPath;
    private String releaseDate;
    private double voteAverage;
    private double popularity;
    private String backDropPath;
    private long voteCount;

    public Movie(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getBackDropPath() {
        return backDropPath;
    }

    public void setBackDropPath(String backDropPath) {
        this.backDropPath = backDropPath;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(long voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // The following has to be read in the same order in Parcelable.Creator
        dest.writeInt(id);
        dest.writeLong(movieId);
        dest.writeString(title);
        dest.writeString(synopsis);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeString(backDropPath);
        dest.writeLong(voteCount);
    }

    public Movie(Parcel input){
        // Read the same as it was written
        id=input.readInt();
        movieId=input.readLong();
        title=input.readString();
        synopsis=input.readString();
        posterPath=input.readString();
        releaseDate=input.readString();
        voteAverage=input.readDouble();
        popularity=input.readDouble();
        backDropPath=input.readString();
        voteCount=input.readLong();
    }

    public static final Creator<Movie> CREATOR
            = new Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            Log.d("Movie", "Create from parcel");
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
