package com.example.user.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * Created by user on 10/13/2016.
 */
public class DetailActivity extends AppCompatActivity {
    ImageView ivw;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.d("check_i",extras.toString());
        String titlet=extras.getString("title");
        String bkg=extras.getString("thumb");
        //Log.d("check_title",titlet);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(titlet);
        }
        ivw=(ImageView) findViewById(R.id.poster_image_view);
        ivw.setVisibility(View.VISIBLE);
        putBackground(bkg);
        TextView tv1=(TextView)findViewById(R.id.rating_text_view);
        tv1.setText("Rating:"+extras.getString("rating"));
        TextView tv2=(TextView)findViewById(R.id.date_text_view);
        tv2.setText("Release Date:"+extras.getString("release"));
        TextView tv3=(TextView)findViewById(R.id.overview_text_view);
        tv3.setText(extras.getString("plot"));





    }
    public void putBackground(String bkgurl)
    {
        String url = "http://image.tmdb.org/t/p/w185"+bkgurl;
        Picasso.with(DetailActivity.this).setLoggingEnabled(true);

        Picasso.with(DetailActivity.this).load(url).into(ivw);


    }
}