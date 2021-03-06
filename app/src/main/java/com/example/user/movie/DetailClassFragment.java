package com.example.user.movie;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.user.movie.data.MovieContract;
import com.example.user.movie.data.ReviewAdapter;
import com.example.user.movie.data.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by KV on 25/1/17.
 */

public class DetailClassFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String DETAIL_URI = "URI";
    static final String Tag_movid = "movieid";
    private Uri mUri;
    ImageView ivw;
    TextView tv0, tv1, tv2, tv3;
    String selectedMovieId;
    String title;
    String poster;
    String overview;
    ImageView trailerImage;
    private Trailer mTrailer;
    String date;
    RatingBar ratingBar;
    private ArrayList<Trailer> TrailerList = new ArrayList<Trailer>();
    private ArrayList<Review> ReviewList=new ArrayList<>();
    ImageButton fav;

    int keymovieId;

    String key;

    private ShareActionProvider mShareActionProvider;

    private static final String[] Detail_COLUMNS = {
            MovieContract.MovieC._ID, MovieContract.MovieC.Column_Movieid, MovieContract.MovieC.COLUMN_TITLE, MovieContract.MovieC.COLUMN_IMAGE, MovieContract.MovieC.COLUMN_bkgIMAGE, MovieContract.MovieC.COLUMN_OVERVIEW, MovieContract.MovieC.COLUMN_RATING, MovieContract.MovieC.COLUMN_DATE
    };
    private static final String[] Trailer_COLUMNS = {
            MovieContract.TrailerC._ID, MovieContract.TrailerC.Column_Movieid, MovieContract.TrailerC.Column_MovieKey
    };
    private static final String[] Favourite_Columns={MovieContract.FavoriteC._ID, MovieContract.FavoriteC.Column_Movieid, MovieContract.FavoriteC.COLUMN_TITLE, MovieContract.FavoriteC.COLUMN_IMAGE, MovieContract.FavoriteC.COLUMN_bkgIMAGE, MovieContract.FavoriteC.COLUMN_OVERVIEW, MovieContract.FavoriteC.COLUMN_RATING, MovieContract.FavoriteC.COLUMN_DATE};
    private static final String[] Review_COLUMNS = {MovieContract.ReviewC._ID, MovieContract.ReviewC.Column_Movieid, MovieContract.ReviewC.Column_author, MovieContract.ReviewC.Column_content, MovieContract.ReviewC.Column_reviewid};

    //public String mSort;
    static final int Col_ID = 0;
    static final int Col_MovieId = 1;
    static final int Col_MovieTitle = 2;
    static final int Col_MoviePoster = 3;
    static final int Col_Moviebkg = 4;
    static final int Col_MovieOverview = 5;
    static final int Col_MovieRating = 6;
    static final int Col_MovieDate = 7;
    String tag_Movieid = "TAGMovieID";
    static final int Col_TRAID = 0;
    static final int Col_TRAMID = 1;
    static final int Col_TRAIMKEY = 2;
    static final int Col_RevieId = 0;
    static final int Col_author = 2;
    static final int Col_content = 3;
    static final int Col_reviewId = 4;
    public static final String TAG = DetailClassFragment.class.getSimpleName();
    boolean favchk;

    final int Detail_Loader = 0;
    final int Trailer_Loader = 1;
    final int Review_Loader=2;
    private RecyclerView mRecyclerView;
    private TrailerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView rRecyclerView;
    private RecyclerView.Adapter rAdapter;
    private RecyclerView.LayoutManager rLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailClassFragment.DETAIL_URI);
            selectedMovieId = MovieContract.MovieC.getIdFromUri(mUri);
            keymovieId = arguments.getInt(DetailClassFragment.Tag_movid);
            Log.d("movid", "id is" + keymovieId);


        }
    }

    public void onActivityCreated(Bundle args) {
        if (keymovieId != 0) {
            TrailerClass Tc = new TrailerClass();
            String s = Integer.toString(keymovieId);
            Tc.execute(s);
            ReviewClass Rc=new ReviewClass();
            Rc.execute(s);
            getLoaderManager().initLoader(Trailer_Loader, null, this);

            getLoaderManager().initLoader(Detail_Loader, null, this);
            getLoaderManager().initLoader(Review_Loader,null,this);




        }

        super.onActivityCreated(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.activity_detailfragment, container, false);

        LinearLayout Detaillay=(LinearLayout) v.findViewById(R.id.detailay);


        ivw = (ImageView) v.findViewById(R.id.poster_image_view);

        ivw.setVisibility(View.VISIBLE);
        tv0 = (TextView) v.findViewById(R.id.moviedetail_title);

        tv1 = (TextView) v.findViewById(R.id.rating_text_view);

        tv2 = (TextView) v.findViewById(R.id.date_text_view);

        tv3 = (TextView) v.findViewById(R.id.overview_text_view);
        fav=(ImageButton) v.findViewById(R.id.favbut);
        ratingBar=(RatingBar) v.findViewById(R.id.ratingbar);
        // trailerImage=(ImageView)v.findViewById(R.id.imgTrailer);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.trailer_recycleview);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        rRecyclerView = (RecyclerView) v.findViewById(R.id.review_recycleview);
        rLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rRecyclerView.setHasFixedSize(true);
        rRecyclerView.setLayoutManager(rLayoutManager);
        rRecyclerView.setItemAnimator(new DefaultItemAnimator());
        String selectionk = MovieContract.FavoriteC.Column_Movieid + "=?";
        String[] selectionArgs = new String[]{String.valueOf(keymovieId)};
        Cursor cur=getContext().getContentResolver().query(MovieContract.FavoriteC.Content_Uri,Favourite_Columns,selectionk,selectionArgs,null);
        if(cur.getCount()>0)
        {
            fav.setImageResource(R.drawable.ic_favorite_black_36dp);
            favchk=true;
        }


        if (keymovieId != 0) {
            //nestedScrollView.setVisibility(View.VISIBLE);
           Detaillay.setVisibility(View.VISIBLE);

        }




        return v;


    }




    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        switch (id) {
            case 0:
                if (mUri != null) {
                    String[] selectionArgs;
                    String selection;
                    String path=mUri.getPathSegments().get(0);
                    if(path.equals(MovieContract.FavoriteC.tableName))
                    {
                        selection = MovieContract.FavoriteC.Column_Movieid + "=?";
                        selectionArgs=new String[]{String.valueOf(keymovieId)};
                        return new CursorLoader(getActivity(), MovieContract.FavoriteC.Content_Uri, Favourite_Columns, selection, selectionArgs, null);
                       // selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mUri))};

                    }
                    else {


                        selection = MovieContract.MovieC.Column_Movieid + "=?";
                        //selectionArgs = new String[]{String.valueOf(ContentUris.parseId(mUri))};
                         selectionArgs=new String[]{String.valueOf(keymovieId)};
                        Log.d("checkLoadervalue", "val is" + selectionArgs);
                        return new CursorLoader(getActivity(), MovieContract.MovieC.Content_Uri, Detail_COLUMNS, selection, selectionArgs, null);
                    }


                }

            case 1:
                if (mUri != null) {
                    String selectionk = MovieContract.TrailerC.Column_Movieid + "=?";
                    String[] selectionArgs = new String[]{String.valueOf(keymovieId)};
                    return new CursorLoader(getActivity(), MovieContract.TrailerC.Content_Uri, Trailer_COLUMNS, selectionk, selectionArgs, null);
                }
            case 2:
                if(mUri!=null) {
                    String selectionk = MovieContract.ReviewC.Column_Movieid + "=?";
                    String[] selectionArgs = new String[]{String.valueOf(keymovieId)};
                    return new CursorLoader(getActivity(), MovieContract.ReviewC.Content_Uri, Review_COLUMNS, selectionk, selectionArgs, null);
                }


        }

        return null;

    }

    @Override

    public void onLoadFinished(Loader loader, Cursor data) {

        int loaderId = loader.getId();
        switch (loaderId) {
            case Detail_Loader: {

                if (data != null && data.moveToFirst()) {
                    String titlet = data.getString(Col_MovieTitle);
                    // int movieId=data.getInt(Col_MovieId);
                    // TrailerClass Tc=new TrailerClass();

                    // Tc.execute(Integer.toString(movieId));
                    // Log.d("selected","mov is"+movieId);

                    android.app.ActionBar actionBar = getActivity().getActionBar();
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                        actionBar.setTitle(titlet);
                    }
                     poster=data.getString(Col_MoviePoster);
                    final String bkgurl = data.getString(Col_Moviebkg);
                    String url = "http://image.tmdb.org/t/p/w342" + bkgurl;
                    Log.d("bkcheck",url);
                    Picasso.with(getContext()).setLoggingEnabled(true);
                    Picasso.with(getContext()).load(url).into(ivw);
                   final int rat=data.getInt(Col_MovieRating);
                    float rati= (float) ((rat/10.0)*5.0);
                   overview=data.getString(Col_MovieOverview);
                    //int rating=Integer.parseInt(rat);
                    Log.d("ratingval","rating is"+rati);
                    title=data.getString(Col_MovieTitle);
                    date=data.getString(Col_MovieDate);

                    tv0.setText(data.getString(Col_MovieTitle));
                    tv1.setText(data.getString(Col_MovieRating));
                    tv2.setText(data.getString(Col_MovieDate));
                    tv3.setText(data.getString(Col_MovieOverview));
                    ratingBar.setRating(rati);
                    fav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           String selectionk = MovieContract.FavoriteC.Column_Movieid + "=?";
                            String[] selectionArgs = new String[]{String.valueOf(keymovieId)};
                           // Cursor cur=getContext().getContentResolver().query(MovieContract.FavoriteC.Content_Uri,Favourite_Columns,selectionk,selectionArgs,null);
                            if(favchk)
                            {
                                getContext().getContentResolver().delete(MovieContract.FavoriteC.Content_Uri,selectionk,selectionArgs);
                                Log.d("favdel","indelete");

                                fav.setImageResource(R.drawable.ic_favorite_border_black_36dp);
                                favchk=false;


                            }
                            else
                            {

                                fav.setImageResource(R.drawable.ic_favorite_black_36dp);
                                ContentValues cv=new ContentValues();
                                cv.put(MovieContract.FavoriteC.Column_Movieid,keymovieId);
                                cv.put(MovieContract.FavoriteC.COLUMN_IMAGE,poster);
                                cv.put(MovieContract.FavoriteC.COLUMN_TITLE,title);
                                cv.put(MovieContract.FavoriteC.COLUMN_bkgIMAGE,bkgurl);
                                cv.put(MovieContract.FavoriteC.COLUMN_RATING,rat);
                                cv.put(MovieContract.FavoriteC.COLUMN_OVERVIEW,overview);
                                cv.put(MovieContract.FavoriteC.COLUMN_DATE,date);
                                Uri uri=getContext().getContentResolver().insert(MovieContract.FavoriteC.Content_Uri,cv);
                                Log.d("favdb","res"+uri);
                                favchk=true;


                            }

                        }
                    });



                }
                return;


            }
            case Trailer_Loader: {

                int count = data.getCount();
                Log.d("countcal", "tots" + count);
                TrailerList.clear();
                data.moveToFirst();
                while (count > 0) {
                    String mkey = data.getString(Col_TRAIMKEY);
                    Trailer trailer = new Trailer(mkey);
                    TrailerList.add(trailer);
                    data.moveToNext();
                    count -= 1;
                }


                Log.d("LOG_TLIST", "TRAILER" + TrailerList);
                mAdapter = new TrailerAdapter(TrailerList, getContext());
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new TrailerAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position, View v) {
                        Trailer item = TrailerList.get(position);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + item.getKey()));
                        startActivity(intent);
                    }
                });



              /*  if (data.getCount() > 0) {
                    data.moveToFirst();
                    key = data.getString(Col_TRAIMKEY);
                    String yt_url = "http://img.youtube.com/vi/" + key + "/0.jpg";
                    Log.d("checktrailerurl", yt_url);
                    Picasso.with(getContext()).setLoggingEnabled(true);
                    Picasso.with(getContext()).load(yt_url).into(trailerImage);


                    trailerImage.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + key));
                            startActivity(intent);
                        }

                    });
                }*/
                return;


            }
            case Review_Loader:
            {

                int count = data.getCount();
                Log.d("countrev", "tots" + count);
                ReviewList.clear();
                data.moveToFirst();
                while (count > 0) {
                    String author = data.getString(Col_author);
                    String content=data.getString(Col_content);
                    Review review = new Review(author,content);
                    ReviewList.add(review);
                    data.moveToNext();
                    count -= 1;
                }


                Log.d("LOG_TLIST", "TRAILER" + TrailerList);
                rAdapter = new ReviewAdapter(ReviewList, getContext());
                rRecyclerView.setAdapter(rAdapter);



                return;


            }

            default: {
                Log.d("lOADER ERROR", "not able to display cursor for any loaders");
            }


        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public class TrailerClass extends AsyncTask<String, Void, List<Trailer>> {
        @Override
        protected List<Trailer> doInBackground(String... params) {


            HttpURLConnection httpURLConnection = null;
            BufferedReader br = null;
            String movieJsonStr = null;


            try {
                final String API_KEY_PARAM = "api_key";
                final String Base_url = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                Uri uri = Uri.parse(Base_url).buildUpon().appendQueryParameter(API_KEY_PARAM, getString(R.string.tmdb_api_key)).build();
                URL url = new URL(uri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJsonStr = null;
                }
                br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();
                Log.d("tag_check", "movie json" + movieJsonStr);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
            try {
                getmovieTrailerFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e("log_t", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;


        }

        List<Trailer> getmovieTrailerFromJson(String jsonStr) throws JSONException {
            JSONObject trailerJson = new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> results = new ArrayList<>();
            final String KEY = "key";
            Trailer trailerModel;


            for (int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                if (trailer.getString("site").contentEquals("YouTube")) {
                    String trailerkey = trailer.getString(KEY);
                    Log.d("keycheck", trailerkey);
                    trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                    String selection = MovieContract.TrailerC.Column_Movieid + "=?";
                    String[] selectionArgs = new String[]{String.valueOf(keymovieId)};
                    // Cursor cur = null;
                    // cur = getContext().getContentResolver().query(MovieContract.TrailerC.Content_Uri, Trailer_COLUMNS, selection, selectionArgs, null);
                    // if (cur.moveToFirst()) {
                    // break;


                    // } else {

                    ContentValues cv = new ContentValues();
                    cv.put(MovieContract.TrailerC.Column_Movieid, keymovieId);
                    cv.put(MovieContract.TrailerC.Column_MovieKey, trailerModel.getKey());
                    Uri ur;

                    ur = getContext().getContentResolver().insert(MovieContract.TrailerC.Content_Uri, cv);
                    Log.d("CheckJson", trailerModel.getName());
                    Log.d("inserted ur", "uri is" + ur);
                    // }


                }


            }
            Log.d("charraylist", "lis" + results);
            return results;
        }
       /* @Override
        protected void onPostExecute(List<Trailer> trailers) {

            Log.d("inFunc","onpOST");
            if (trailers != null) {
                if (trailers.size() > 0) {

                      //  for (Trailer trailer : trailers) {
                         //   mTrailerAdapter.add(trailer);
                       // }
                    }

                    mTrailer = trailers.get(0);
                Log.d("checkt","trail"+mTrailer);
                key= mTrailer.getKey();
                Log.d("keyvalue",key);


                }

            }
            */
    }

    public class ReviewClass extends AsyncTask<String, Void, List<Review>> {
        String TAG = ReviewClass.class.getName();

        @Override
        protected List<Review> doInBackground(String... params) {
            HttpURLConnection httpURLConnection = null;
            BufferedReader br = null;
            String movieJsonStr = null;
            try {
                final String API_KEY_PARAM = "api_key";
                final String Base_url = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                Uri uri = Uri.parse(Base_url).buildUpon().appendQueryParameter(API_KEY_PARAM, getString(R.string.tmdb_api_key)).build();
                URL url = new URL(uri.toString());
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJsonStr = null;
                }
                br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();
                Log.d(TAG, "movie json" + movieJsonStr);


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (httpURLConnection != null)
                    httpURLConnection.disconnect();
            }
            if (br != null) {
                try {
                    br.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
            try {
                getmovieReviewFromJson(movieJsonStr);
            }
            catch (JSONException e) {
                Log.e("log_tm", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;


        }


        List<Review> getmovieReviewFromJson(String ReviewJson) throws JSONException {
            JSONObject reviewJson = new JSONObject(ReviewJson);
            JSONArray reviewArray = reviewJson.getJSONArray("results");

            List<Review> results = new ArrayList<>();

            for(int i = 0; i < reviewArray.length(); i++) {
                JSONObject review = reviewArray.getJSONObject(i);
                Review revobj=new Review(review);
                results.add(revobj);
                ContentValues cv = new ContentValues();
                cv.put(MovieContract.ReviewC.Column_Movieid, keymovieId);
                cv.put(MovieContract.ReviewC.Column_author,revobj.getAuthor() );
                cv.put(MovieContract.ReviewC.Column_content,revobj.getContent());
                cv.put(MovieContract.ReviewC.Column_reviewid,revobj.getId());
                Uri ur;

                ur = getContext().getContentResolver().insert(MovieContract.ReviewC.Content_Uri, cv);
            }

            return results;
        }

        }


    }


