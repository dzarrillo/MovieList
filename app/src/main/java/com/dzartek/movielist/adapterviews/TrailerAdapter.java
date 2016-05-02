package com.dzartek.movielist.adapterviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dzartek.movielist.R;
import com.dzartek.movielist.datamodel.MovieConst;
import com.dzartek.movielist.datamodel.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<MovieTrailer> trailerList;

    public TrailerAdapter(Context context, ArrayList<MovieTrailer> trailerList){
        inflater = LayoutInflater.from(context);
        this.trailerList = trailerList;
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.trailer_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, int position) {
        MovieTrailer currentVideo = trailerList.get(position);
        Context context = holder.trailer.getContext();
        String img_url = MovieConst.BASE_YOUTUBE_URL + currentVideo.getKey() + "/0.jpg";
        Picasso.with(context)
                .load(img_url)
                .resize(200, 300)
                .error(R.drawable.female_film)
                .centerInside()
                .into(holder.trailer);
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView trailer;

        public MyViewHolder(View itemView) {
            super(itemView);
            trailer = (ImageView) itemView.findViewById(R.id.imageTrailer);
        }
    }
}
