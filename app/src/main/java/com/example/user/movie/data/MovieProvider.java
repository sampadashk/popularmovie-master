package com.example.user.movie.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;


/**
 * Created by KV on 19/1/17.
 */

public class MovieProvider extends ContentProvider {

    final int Movie=10;
    final int Trailer=20;
   public static MovieDb movieDatabase;
   public static final UriMatcher uriMatcher=buildUriMatcher();
    @Override
    public boolean onCreate() {
        movieDatabase=new MovieDb(getContext());
           return true;
    }

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher umatcher=new UriMatcher(UriMatcher.NO_MATCH);
        umatcher.addURI(MovieContract.Content_Authority,MovieContract.path_movie,10);
        umatcher.addURI(MovieContract.Content_Authority,MovieContract.path_trailer,20);
        return umatcher;

    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (uriMatcher.match(uri))
        {
            case Movie: {

                retCursor = movieDatabase.getReadableDatabase().query(MovieContract.MovieC.tableName, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
            case Trailer:
            {
                retCursor=movieDatabase.getReadableDatabase().query(MovieContract.TrailerC.tableName,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
           return retCursor;
        }





    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case Movie: {
                return MovieContract.MovieC.Content_Type;

            }
            case Trailer:{
                return MovieContract.TrailerC.Content_Type;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }


        @Override
        public Uri insert (Uri uri, ContentValues values){
            final SQLiteDatabase db = movieDatabase.getWritableDatabase();
            Uri resultUri;
            switch (uriMatcher.match(uri)) {
                case Movie: {
                    long id = db.insert(MovieContract.MovieC.tableName, null, values);
                    if (id > 0) {
                        resultUri = MovieContract.MovieC.BuildUriFromId(id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                    break;


                }
                case Trailer: {
                    long id = db.insert(MovieContract.TrailerC.tableName, null, values);
                    if (id > 0) {
                        resultUri = MovieContract.TrailerC.BuildUriFromId(id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                    break;


                }

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return resultUri;


        }

        @Override
        public int delete (Uri uri, String selection, String[]selectionArgs){
            final SQLiteDatabase db = movieDatabase.getWritableDatabase();
            int rowsDeleted;
            // this makes delete all rows return the number of rows deleted
            if (null == selection) selection = "1";
            switch (uriMatcher.match(uri)) {
                case Movie:
                    rowsDeleted = db.delete(
                            MovieContract.MovieC.tableName, selection, selectionArgs);
                    break;
                case Trailer:
                    rowsDeleted = db.delete(
                            MovieContract.TrailerC.tableName, selection, selectionArgs);
                    break;

                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            // Because a null deletes all rows
            if (rowsDeleted != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsDeleted;
        }

        @Override
        public int update (Uri uri, ContentValues values, String selection, String[]selectionArgs){
            final SQLiteDatabase db = movieDatabase.getWritableDatabase();
            int rowsUpdated;

            switch (uriMatcher.match(uri)) {
                case Movie:
                    rowsUpdated = db.update(MovieContract.MovieC.tableName, values, selection,
                            selectionArgs);
                    break;
                case Trailer:
                    rowsUpdated = db.update(MovieContract.TrailerC.tableName, values, selection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }

            return rowsUpdated;
        }
    @Override
    public int bulkInsert(Uri uri,ContentValues[] values)
    {
        final SQLiteDatabase db=movieDatabase.getWritableDatabase();
        final int match=uriMatcher.match(uri);
        switch (match)
        {
            case Movie: {
                db.beginTransaction();
                int retcount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(MovieContract.MovieC.tableName, null, value);
                        if (id != -1) {

                            retcount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return retcount;
            }
            case Trailer: {
                db.beginTransaction();
                int retcount = 0;
                try {
                    for (ContentValues value : values) {
                        long id = db.insert(MovieContract.TrailerC.tableName, null, value);
                        if (id != -1) {

                            retcount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return retcount;
            }

            default:
                return super.bulkInsert(uri,values);

        }

    }

        }


