package com.example.user.movie;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.user.movie.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Vector;


public class MovieFragment extends android.support.v4.app.Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private MovieAdapter imageArrayAdapter;

    private static final int Movie_Loader=0;
    private static final int Favourite_Loader=1;
    private static final String[] MOVIE_COLUMNS={
            MovieContract.MovieC._ID,MovieContract.MovieC.Column_Movieid,MovieContract.MovieC.COLUMN_TITLE,MovieContract.MovieC.COLUMN_IMAGE,MovieContract.MovieC.COLUMN_bkgIMAGE,MovieContract.MovieC.COLUMN_OVERVIEW,MovieContract.MovieC.COLUMN_RATING,MovieContract.MovieC.COLUMN_DATE
    };
    public String mSort;
    static final int Col_ID=0;
    static final int Col_MovieId=1;
    static final int Col_MovieTitle=2;
    static final int Col_MoviePoster=3;
    static final int Col_Moviebkg=4;
    static final int Col_MovieOverview=5;
    static final int Col_MovieRating=6;
    static final int Col_MovieDate=7;
   // String tag_Movieid="TAGMovieID";
    String selectedMovieId;
    ImageView trailerImage;
    //private Trailer mTrailer;

    List<Trailer> trailerList;
    String keyofTrailer;

    //ImageArray[] moviesend;
    private List<ImageArray> movies;
    GridView gridview;
    int mPosition=GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";


    public MovieFragment() {
        // Required empty public constructor
    }
    public interface Callback {
        void onItemSelected(Uri uri,int mId);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
      //  mSort = sharedPrefs.getString(
           //     getString(R.string.orderkey),
             //   getString(R.string.defaultval));

       // displayMovie();


    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.menuitem) {

            startActivityForResult(new Intent(getContext(),SettingsActivity.class),0);
            return true;
        }
        return super.onOptionsItemSelected(menuItem);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {

       SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
         mSort = sharedPrefs.getString(
                getString(R.string.orderkey),
                getString(R.string.defaultval));
        if(mSort.equals(getString(R.string.pref_units_fav)))
        {
            getLoaderManager().initLoader(Favourite_Loader,null,this);
        }
        else {
            getLoaderManager().initLoader(Movie_Loader, null, this);

        }

        super.onActivityCreated(savedInstanceState);

    }
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case 0: {
                Uri MovieUri = MovieContract.MovieC.Content_Uri;
                Log.d("LOadercheck", "create working");

                return new android.support.v4.content.CursorLoader(getActivity(), MovieUri, MOVIE_COLUMNS, null, null, null);

            }
            case 1: {
                Uri FavouriteUri = MovieContract.FavoriteC.Content_Uri;
                return new android.support.v4.content.CursorLoader(getActivity(), FavouriteUri, MOVIE_COLUMNS, null, null, null);

            }
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {
        int loaderId = loader.getId();
        switch (loaderId) {
            case Movie_Loader: {
                Log.d("LOaderfinishcheck", "finish working");
                Log.d("cursorlength", "lenght is" + cursor.getCount());


                imageArrayAdapter.swapCursor(cursor);
                gridview.setAdapter(imageArrayAdapter);

                gridview.setVisibility(View.VISIBLE);
                gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {



                    public void onItemClick(AdapterView<?> adapterview,View view,int i,long l)
                    {
                        Cursor cursor=(Cursor)adapterview.getItemAtPosition(i);
                        if(cursor!=null) {



                            Log.d("cursorvaluemain","val"+cursor.getInt(Col_ID));
                            int movieId=cursor.getInt(Col_MovieId);
                            Log.d("Tsgmoviesel","selected"+movieId);


                            Uri ur=MovieContract.MovieC.BuildUriFromId(cursor.getInt(Col_ID));
                            ((Callback)getActivity()).onItemSelected(ur,movieId);


                        }
                        mPosition=i;


                    }



                });
                Log.d("adaptersize", "adaptersixe" + imageArrayAdapter.getCount());
                if (mPosition != GridView.INVALID_POSITION) {
                    gridview.smoothScrollToPosition(mPosition);
                }
                break;


            }
            case Favourite_Loader: {
                Log.d("loaderchk","in fav");


                    imageArrayAdapter.swapCursor(cursor);
                    gridview.setAdapter(imageArrayAdapter);
                    gridview.setVisibility(View.VISIBLE);

                    gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                    {



                        public void onItemClick(AdapterView<?> adapterview,View view,int i,long l)
                        {
                            Cursor cursor=(Cursor)adapterview.getItemAtPosition(i);
                            if(cursor!=null) {


                                int movieId=cursor.getInt(Col_MovieId);
                                Log.d("favmoviesel","selected"+movieId);

                                Uri ur=MovieContract.FavoriteC.BuildUriFromId(cursor.getInt(Col_ID));
                                Log.d("favurl","urlcheck"+ur);
                                String path=ur.getPathSegments().get(0);
                                Log.d("pathchk",path);
                                ((Callback)getActivity()).onItemSelected(ur,movieId);


                            }
                            mPosition=i;


                        }



                    });
                    Log.d("adaptersize", "adaptersixe" + imageArrayAdapter.getCount());
                    if (mPosition != GridView.INVALID_POSITION) {
                        gridview.smoothScrollToPosition(mPosition);
                    }


                break;

            }
        }
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        imageArrayAdapter.swapCursor(null);
    }




    public void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPrefs.getString(
                getString(R.string.orderkey),
                getString(R.string.defaultval));
        if (!mSort.equals(sortOrder)&&(!sortOrder.equals(getString(R.string.pref_units_fav)))) {
            getLoaderManager().restartLoader(Movie_Loader, null, this);
            //  getLoaderManager().getLoader(Movie_Loader).
            getContext().getContentResolver().delete(MovieContract.MovieC.Content_Uri, null, null);


            mSort = sortOrder;
            displayMovie();
          /*  if (!mSort.equals(getString(R.string.pref_units_fav))) {


                displayMovie();

            }
            */



        }
        else if(sortOrder.equals(getString(R.string.pref_units_fav)))
        {
            getLoaderManager().restartLoader(Favourite_Loader,null,this);
            mSort=sortOrder;
        }



    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
       // movies = new ArrayList<ImageArray>();

        //imageArrayAdapter = new MovieAdapter(getActivity(), movies);
        imageArrayAdapter=new MovieAdapter(getActivity(),null,0);
        gridview = (GridView) rootView.findViewById(R.id.grd_view);

        if(savedInstanceState!=null&&savedInstanceState.containsKey(SELECTED_KEY))
        {
            mPosition=savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;
    }

  public void onStart() {
        displayMovie();
        super.onStart();

    }
    @Override
    public void onSaveInstanceState(Bundle outstate)
    {
        if(mPosition!=GridView.INVALID_POSITION)
        {
            outstate.putInt(SELECTED_KEY,mPosition);
        }
        super.onSaveInstanceState(outstate);
    }


    private void displayMovie() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = sharedPrefs.getString(
                getString(R.string.orderkey),
                getString(R.string.defaultval));
        String QUERY_PARAM;
        if (!sortOrder.equals(getString(R.string.pref_units_fav))) {

            FetchMovie ft = new FetchMovie();
            ft.execute(sortOrder);
        }
    }

    class FetchMovie extends AsyncTask<String, Void, Void> {

        ImageArray[] movieArr;


        private void getmovieDataFromJson(String forecastJsonStr)
                throws JSONException {
            final String result = "results";
            final String poster = "poster_path";
            final String TITLE = "title";
            final String origin_name = "original_title";
            final String thumb = "backdrop_path";
            final String synopsis = "overview";
            final String rating = "vote_average";
            final String release = "release_date";
            final String popular = "popularity";
            final String movieId = "id";

            JSONObject jsobject = new JSONObject(forecastJsonStr);
            JSONArray jsarray = jsobject.getJSONArray(result);
            int n = jsarray.length();
            movieArr = new ImageArray[n];
            Vector<ContentValues> cVector = new Vector<>(jsarray.length());
            // moviesend=new ImageArray[n];


            String movieName;
            String posterpath;
            String movieTitle;
            String releaseDate;
            String ratings;
            String plot;
            String thumbs;
            int Id;


            //ImageView imageView=(ImageView) view.findViewById(R.id.img_view);
            try {
                for (int i = 0; i < jsarray.length(); i++) {


                    JSONObject popmovie = jsarray.getJSONObject(i);
                    movieName = popmovie.getString(TITLE);
                    posterpath = popmovie.getString(poster);
                    movieTitle = popmovie.getString(origin_name);
                    releaseDate = popmovie.getString(release);
                    ratings = popmovie.getString(rating);
                    plot = popmovie.getString(synopsis);
                    thumbs = popmovie.getString(thumb);
                    Id = popmovie.getInt(movieId);


                    ContentValues values = new ContentValues();
                    values.put(MovieContract.MovieC.Column_Movieid, Id);
                    values.put(MovieContract.MovieC.COLUMN_TITLE, movieTitle);
                    values.put(MovieContract.MovieC.COLUMN_IMAGE, posterpath);
                    values.put(MovieContract.MovieC.COLUMN_bkgIMAGE, thumbs);
                    values.put(MovieContract.MovieC.COLUMN_OVERVIEW, plot);
                    values.put(MovieContract.MovieC.COLUMN_RATING, ratings);
                    values.put(MovieContract.MovieC.COLUMN_DATE, releaseDate);
                    cVector.add(values);

                    //uncheck this
                    // movieArr[i] = new ImageArray(movieName, moviePosters,movieTitle,releaseDate,thumbs,ratings,plot,Id);


                    // moviesend[i]=new ImageArray(movieTitle,releaseDate,thumbs,ratings,plot);


                }
                int inserted = 0;
                if (cVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVector.size()];
                    cVector.toArray(cvArray);
                    inserted = getContext().getContentResolver().bulkInsert(MovieContract.MovieC.Content_Uri, cvArray);
                }
                Log.d("CheckInsert", "inserted is" + inserted);
            } catch (JSONException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }


        }

        protected Void doInBackground(String... params) {
            HttpURLConnection con = null;
            BufferedReader br = null;
            String forecastJsonStr = null;
            String sortOrder = params[0];
            Log.d("ordercheck", sortOrder);
            String QUERY_PARAM;
            if (sortOrder.equals("vote_average")) {
                QUERY_PARAM = "top_rated";
            } else

                QUERY_PARAM = "popular";

            String appKey = " ";
            // TODO: Put Your apikey here in variable appKey
            try {


                final String APPID_PARAM = "api_key";

                String add = "http://api.themoviedb.org/3/movie/" + QUERY_PARAM + "?";

                Uri ur = Uri.parse(add).buildUpon().appendQueryParameter(APPID_PARAM, appKey).build();
                URL url = new URL(ur.toString());
                Log.d("check_u", ur.toString());
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                InputStream ip = con.getInputStream();
                if (ip == null) {
                    // Nothing to do.
                    forecastJsonStr = null;
                }
                br = new BufferedReader(new InputStreamReader(ip));

                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = br.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                }
                forecastJsonStr = buffer.toString();
                Log.d("tag_check", "forecast json" + forecastJsonStr);


            } catch (IOException ie) {
                return null;
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (br != null) {
                    try {
                        br.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            try {
                getmovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                Log.e("log_t", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }
    }


}




