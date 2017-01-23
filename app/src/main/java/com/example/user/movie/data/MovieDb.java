package com.example.user.movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KV on 19/1/17.
 */

public class MovieDb extends SQLiteOpenHelper {
    public static final int Database_version=1;
    public static final String Database_name="movie.db";
   public MovieDb(Context context) {
        super(context,Database_name,null,Database_version);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.MovieC.tableName + " (" +
                MovieContract.MovieC._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieC.Column_Movieid + " INTEGER UNIQUE NOT NULL, " +
                MovieContract.MovieC.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieC.COLUMN_IMAGE + " TEXT, " +
                MovieContract.MovieC.COLUMN_bkgIMAGE + " TEXT, " +
                MovieContract.MovieC.COLUMN_OVERVIEW + " TEXT, " +
                MovieContract.MovieC.COLUMN_RATING + " INTEGER, " +
                MovieContract.MovieC.COLUMN_DATE + " TEXT);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS"+MovieContract.MovieC.tableName);
        onCreate(db);
    }
}
