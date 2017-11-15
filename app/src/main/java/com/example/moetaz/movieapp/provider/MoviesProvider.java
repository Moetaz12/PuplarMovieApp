package com.example.moetaz.movieapp.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.example.moetaz.movieapp.datastorage.DBAdadpter;

/**
 * Created by Moetaz on 9/23/2017.
 */

public class MoviesProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.moetaz.movieapp";

    public static final String PATH_MOVIES_LIST = "MOVIES_LIST";
    public static final String PATH_MOVIE_ID = "MOVIE_ID";

    public static final int MOVIES_LIST = 1;
    public static final int MOVIE_ID = 2;

    public static final String MIME_TYPE_1 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+"vnd.com.moetaz.movies";
    public static final String MIME_TYPE_2 = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+"vnd.com.moetaz.movieid";

    public static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        MATCHER.addURI(AUTHORITY, PATH_MOVIES_LIST,MOVIES_LIST);
        MATCHER.addURI(AUTHORITY, PATH_MOVIE_ID,MOVIE_ID);
    }
    private DBAdadpter dbAdadpter;


    @Override
    public boolean onCreate() {
        dbAdadpter = DBAdadpter.getDBAdadpterInstance(getContext());
        return true ;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)throws UnsupportedOperationException {
        Cursor cursor = null;
        switch (MATCHER.match(uri)){
            case MOVIES_LIST:  cursor = dbAdadpter.getCursorsForAllData(); break;
            case MOVIE_ID : cursor = dbAdadpter.getCursorForId(selectionArgs[0]); break;

            default: cursor = null; break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (MATCHER.match(uri)){
            case MOVIES_LIST:return MIME_TYPE_1;
            case MOVIE_ID :return MIME_TYPE_2;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) throws UnsupportedOperationException{
        Uri Returnuri = null;
        switch (MATCHER.match(uri)){

            case MOVIES_LIST : Returnuri = insertMovie(uri,values);break;
            default: new UnsupportedOperationException("Error") ;break;
        }
        return Returnuri;
    }

    private Uri insertMovie(Uri uri, ContentValues values) {
        long id = dbAdadpter.InsertContentValue(values);
        getContext().getContentResolver().notifyChange(uri,null);

        return  Uri.parse("content://"+AUTHORITY+"/"+ PATH_MOVIES_LIST +"/"+id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) throws UnsupportedOperationException {
        int deleteCount = -1;
        switch (MATCHER.match(uri)){

            case MOVIES_LIST : deleteCount = delete(selection,selectionArgs);break;
            default: new UnsupportedOperationException("Error") ;break;
        }
        return deleteCount;
    }

    private int delete(String selection, String[] selectionArgs) {
        return dbAdadpter.deleteFav(selection,selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
