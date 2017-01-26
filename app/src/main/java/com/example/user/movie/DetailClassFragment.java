package com.example.user.movie;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.movie.data.MovieContract;
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
    static final String Tag_movid="movieid";
    private Uri mUri;
    ImageView ivw;
    TextView tv1,tv2,tv3;
    String selectedMovieId;
    ImageView trailerImage;
    private Trailer mTrailer;
    int keymovieId;
    String key;
    private ShareActionProvider mShareActionProvider;

    private static final String[] Detail_COLUMNS={
            MovieContract.MovieC._ID,MovieContract.MovieC.Column_Movieid,MovieContract.MovieC.COLUMN_TITLE,MovieContract.MovieC.COLUMN_IMAGE,MovieContract.MovieC.COLUMN_bkgIMAGE,MovieContract.MovieC.COLUMN_OVERVIEW,MovieContract.MovieC.COLUMN_RATING,MovieContract.MovieC.COLUMN_DATE
    };
    //public String mSort;
    static final int Col_ID=0;
    static final int Col_MovieId=1;
    static final int Col_MovieTitle=2;
    static final int Col_MoviePoster=3;
    static final int Col_Moviebkg=4;
    static final int Col_MovieOverview=5;
    static final int Col_MovieRating=6;
    static final int Col_MovieDate=7;
    String tag_Movieid="TAGMovieID";
    int Detail_Loader=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(DetailClassFragment.DETAIL_URI);
            selectedMovieId = MovieContract.MovieC.getIdFromUri(mUri);
            keymovieId = arguments.getInt(DetailClassFragment.Tag_movid);
            Log.d("movid", "id is" + keymovieId);
            TrailerClass Tc = new TrailerClass();
            String s = Integer.toString(keymovieId);
            Tc.execute(s);


        }
    }
    public void onActivityCreated(Bundle args)
    {
        getLoaderManager().initLoader(Detail_Loader,null,this);
        super.onActivityCreated(args);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        View v=inflater.inflate(R.layout.activity_detailfragment,container,false);
        ivw=(ImageView) v.findViewById(R.id.poster_image_view);
        ivw.setVisibility(View.VISIBLE);

        tv1=(TextView)v.findViewById(R.id.rating_text_view);

        tv2=(TextView)v.findViewById(R.id.date_text_view);

        tv3=(TextView)v.findViewById(R.id.overview_text_view);
        trailerImage=(ImageView)v.findViewById(R.id.imgTrailer);
        return v;


    }
    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mTrailer.getName() + " " +
                "http://www.youtube.com/watch?v=" + mTrailer.getKey());
        return shareIntent;
    }




    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if(mUri!=null)
        {



            String[] selectionArgs;
            String selection = MovieContract.MovieC._ID + "=?";
            selectionArgs = new String[] { String.valueOf(ContentUris.parseId(mUri)) };
            Log.d("checkLoadervalue","val is"+selectionArgs);
            return new CursorLoader(getActivity(),MovieContract.MovieC.Content_Uri,Detail_COLUMNS,selection,selectionArgs,null);
        }
        return null;
    }
    @Override

    public void onLoadFinished(Loader loader, Cursor data) {
        if(data!=null&&data.moveToFirst())
        {
            String titlet=data.getString(Col_MovieTitle);
           // int movieId=data.getInt(Col_MovieId);
           // TrailerClass Tc=new TrailerClass();

           // Tc.execute(Integer.toString(movieId));
           // Log.d("selected","mov is"+movieId);

            android.app.ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle(titlet);
            }
            String bkgurl=data.getString(Col_Moviebkg);
            String url = "http://image.tmdb.org/t/p/w342"+bkgurl;
            Picasso.with(getContext()).setLoggingEnabled(true);
            Picasso.with(getContext()).load(url).into(ivw);
           tv1.setText(data.getString(Col_MovieRating));
            tv2.setText(data.getString(Col_MovieDate));
            tv3.setText(data.getString(Col_MovieOverview));
            String yt_url = "http://img.youtube.com/vi/" + key + "/0.jpg";
            Log.d("checktrailerurl",yt_url);
            Picasso.with(getContext()).setLoggingEnabled(true);
            Picasso.with(getContext()).load(yt_url).into(trailerImage);



            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }
   public class TrailerClass extends AsyncTask<String, Void, List<Trailer>>
    {
        @Override
        protected List<Trailer> doInBackground(String... params) {


            HttpURLConnection httpURLConnection=null;
            BufferedReader br=null;
            String movieJsonStr=null;


            try {
                final String API_KEY_PARAM = "api_key";
                final String Base_url = "http://api.themoviedb.org/3/movie/" +params[0]  + "/videos";
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


            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if(httpURLConnection!=null)
                    httpURLConnection.disconnect();
            }
            if(br!=null)
            {
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
            JSONObject trailerJson =new JSONObject(jsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray("results");

            List<Trailer> results = new ArrayList<>();

            for(int i = 0; i < trailerArray.length(); i++) {
                JSONObject trailer = trailerArray.getJSONObject(i);
                // Only show Trailers which are on Youtube
                if (trailer.getString("site").contentEquals("YouTube")) {
                    Trailer trailerModel = new Trailer(trailer);
                    results.add(trailerModel);
                    Log.d("CheckJson",trailerModel.getName());
                }
            }
            mTrailer = results.get(0);
            key= mTrailer.getKey();
            Log.d("Tag_keyv",key);

            return results;
        }
        @Override
        protected void onPostExecute(List<Trailer> trailers) {
            Log.d("inFunc","onpOST");
            if (trailers != null) {
                if (trailers.size() > 0) {

                      //  for (Trailer trailer : trailers) {
                         //   mTrailerAdapter.add(trailer);
                       // }
                    }

                    mTrailer = trailers.get(0);
                key= mTrailer.getKey();
                Log.d("keyvalue",key);


                }

            }
        }


    }

