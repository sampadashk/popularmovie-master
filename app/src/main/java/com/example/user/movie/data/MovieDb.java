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
      /*  final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerC.tableName + " (" +
                MovieContract.TrailerC._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.TrailerC.Column_Movieid + " INTEGER NOT NULL, " +
                MovieContract.TrailerC.Column_MovieKey + " TEXT NOT NULL);";
                */

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
       // db.execSQL(SQL_CREATE_TRAILER_TABLE);
        
    }
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+MovieContract.MovieC.tableName);
       // db.execSQL("DROP TABLE IF EXISTS "+MovieContract.TrailerC.tableName);
        onCreate(db);
    }

    public void deleteAll(SQLiteDatabase db)
    {
        //SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        db.execSQL("delete from "+ MovieContract.MovieC.tableName);
        db.close();
    }
}
