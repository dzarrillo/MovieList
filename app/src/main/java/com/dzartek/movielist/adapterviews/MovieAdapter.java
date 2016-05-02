package com.dzartek.movielist.adapterviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dzartek.movielist.R;
import com.dzartek.movielist.datamodel.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<Movie> movieList;

    public MovieAdapter(Context context, ArrayList<Movie> movieList){
        inflater = LayoutInflater.from(context);
        this.movieList = movieList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.movies_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie currentMovie = movieList.get(position);
        // We need to get poster here using piccaso
        Context context = holder.poster.getContext();
        Picasso.with(context)
                .load(currentMovie.getPosterPath())
                .resize(500, 500)
                .error(R.drawable.female_film)
                //.centerInside()
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;

        public MyViewHolder(View itemView) {
            super(itemView);
            poster = (ImageView) itemView.findViewById(R.id.imagePoster);
        }
    }
}
