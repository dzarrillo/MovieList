package com.dzartek.movielist.apinetwork;

import android.util.Log;

import com.dzartek.movielist.datamodel.MovieConst;
import com.dzartek.movielist.datamodel.pojo_moviedetails.Moviedetails;
import com.dzartek.movielist.datamodel.pojo_movies.Movies;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dzarrillo on 4/23/2016.
 */
public interface MovieAPI {
    @GET("discover/movie")
    Call<Movies> getMovies(@Query("sort_by") String sort_by,
                           @Query("api_key") String api_key);

    @GET("movie/{id}")
    Call<Moviedetails> getMovieDetails(@Path("id") int movieId, @Query("api_key") String api_key,
                            @Query("append_to_response") String append_to_response);

    class Factory{
        private static MovieAPI service;

        public static MovieAPI getInstance(){
            if(service==null){
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(MovieConst.BASE_URL)  //        http://api.themoviedb.org/3/
                        .build();
                service = retrofit.create(MovieAPI.class);
                //return service;
            //} else{
                //return service;
            }
            return service;
        }
    }
}
