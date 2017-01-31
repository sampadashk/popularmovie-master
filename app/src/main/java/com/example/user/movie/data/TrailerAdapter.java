package com.example.user.movie.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.movie.R;
import com.example.user.movie.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by KV on 30/1/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private static ClickListener clickListener;
    Context context;
    private ArrayList<Trailer> dataSet;

    public TrailerAdapter(ArrayList<Trailer> data, Context context) {

        this.dataSet = data;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.trailerlayout, parent, false);


        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int listPosition) {


        Trailer item = dataSet.get(listPosition);
        Context context = holder.imageViewIcon.getContext();
        String key=item.getKey();
        Log.d("keychad",key);

        String yt_url = "http://img.youtube.com/vi/" + key + "/0.jpg";

        Picasso.with(context)
                .load(yt_url)
                .into(holder.imageViewIcon);


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TrailerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageViewIcon;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.imageViewIcon = (ImageView) view.findViewById(R.id.imgTrailer);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }


    }
}
