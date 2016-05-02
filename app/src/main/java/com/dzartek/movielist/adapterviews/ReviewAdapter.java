package com.dzartek.movielist.adapterviews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dzartek.movielist.R;
import com.dzartek.movielist.datamodel.MovieReview;

import java.util.ArrayList;

/**
 * Created by dzarrillo on 4/22/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder>{
    private LayoutInflater inflater;
    private ArrayList<MovieReview> reveiwList;

    public ReviewAdapter(Context context, ArrayList<MovieReview> reveiwList){
        inflater= LayoutInflater.from(context);
        this.reveiwList=reveiwList;
    }


    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = inflater.inflate(R.layout.review_custom_row, parent, false);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {
        MovieReview currentReview = reveiwList.get(position);

        holder.textViewAuthor.setText(currentReview.getAuthor().trim());
        holder.textViewContent.setText(currentReview.getContent().trim());
    }

    @Override
    public int getItemCount() {
        return reveiwList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewAuthor, textViewContent;

        public MyViewHolder(View itemView){
            super(itemView);
            textViewAuthor= (TextView) itemView.findViewById(R.id.textViewAuthor);
            textViewContent= (TextView) itemView.findViewById(R.id.textViewContent);
        }
    }
}
