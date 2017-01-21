package com.example.user.movie;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by user on 10/7/2016.
 */
//
// public class ArrayAdapterImage extends ArrayAdapter<ImageArray> {
public class ArrayAdapterImage extends CursorAdapter {

    Context context;

/*
ArrayAdapterImage(Activity context, List<ImageArray> imgarr)
{
    super(context,0,imgarr);
    this.context=context;

}
*/
ArrayAdapterImage(Activity context, Cursor c,int flags)
{
    super(context,c,flags);
    this.context=context;

}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view=LayoutInflater.from(context).inflate(R.layout.movieimage,viewGroup,false);
        return view;
    }

    /*

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ImageArray imgar=getItem(position);
            if(convertView==null) {
                LayoutInflater layoutInflater=(LayoutInflater) LayoutInflater
                        .from(context);
                convertView = layoutInflater.inflate(R.layout.movieimage, parent, false);
            }
            ImageView iv=(ImageView)convertView.findViewById(R.id.img_view);
            iv.setImageResource(imgar.image);

            final String MOVIE_BASE_URL = " http://image.tmdb.org/t/p/";
            final String SIZE = "w342";
            final String POSTER_PATH = "poster_path";
            String value;
            // ?????????????????????
            Uri uri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                    .appendPath(SIZE)
                    .appendPath(POSTER_PATH)
                    .build();
    //        Log.d("address",imgar.link);
            Picasso.with(getContext()).setLoggingEnabled(true);
            Picasso.with(getContext()).load(imgar.link).into(iv);
            return  convertView;


        }
        */
    @Override
    public void bindView(View view,Context context,Cursor cursor)
    {
        ImageView iv=(ImageView)view.findViewById(R.id.img_view);

       // iv.setImageResource(cursor.getString(MovieFragment.Col_MoviePoster));

        final String MOVIE_BASE_URL = " http://image.tmdb.org/t/p/w185";
        final String SIZE = "w342";
        final String POSTER_PATH = "poster_path";
        String value;
        // ?????????????????????

        Log.d("ImgCheck",cursor.getString(MovieFragment.Col_MoviePoster));
        String ImageDisplay=MOVIE_BASE_URL+cursor.getString(MovieFragment.Col_MoviePoster);
        Log.d("ImgCheck1",ImageDisplay);
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(ImageDisplay).into(iv);

    }


}
