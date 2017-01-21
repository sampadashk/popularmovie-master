package com.example.user.movie.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by KV on 19/1/17.
 */

public class MovieContract {
    public static final String Content_Authority="com.example.user.movie";
    public static final Uri Base_Uri=Uri.parse("content://"+Content_Authority);
    public static final String path_movie="movie";
    public static class MovieC implements BaseColumns
    {
       public static  Uri Content_Uri=Base_Uri.buildUpon().appendPath(path_movie).build();
        public static final String Content_Type= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+Content_Authority+"/"+path_movie;
        public static final String Content_Item_Type=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+Content_Authority+"/"+path_movie;
        public static final String tableName="movie";
        public static final String Column_Movieid="MovieID";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_bkgIMAGE = "bkgimage";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_DATE = "date";
        public static Uri BuildUriFromId(long id)
        {
            return ContentUris.withAppendedId(Content_Uri,id);
        }

    }

}
