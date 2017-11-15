package com.example.moetaz.movieapp.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moetaz.movieapp.models.MovieModel;

import java.util.ArrayList;

/**
 * Created by Moetaz on 9/20/2017.
 */

public class DBAdadpter {

    private Context context;
    private SQLiteDatabase sqLiteDatabase;
    private static  DBAdadpter dbAdadpter;
    private static final String DB_NAME = "moviesdb.db";
    private static final int DB_VERSION = 5;
    private static final String TABLE_NAME = "offlinetable";
    private static final String TABLE_NAME_2 = "favmovietable";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String TITLE = "title";
    private static final String POSTER_PATH ="poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String BACK_BATH = "backdrop_path";
    private static final String VOTE = "vote_average";
    private static final String MOVIE_ID = "mid";

    private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+" ("
            +ORIGINAL_TITLE+" TEXT PRIMARY KEY , "
            +TITLE+" TEXT ,"
            +POSTER_PATH+" TEXT,"
            +OVERVIEW+" TEXT ,"
            +RELEASE_DATE+" TEXT,"
            +BACK_BATH+" TEXT ,"
            +VOTE+" TEXT ,"
            +MOVIE_ID+" TEXT )"
            ;

    private static final String CREATE_FAV_TABLE = "CREATE TABLE "+TABLE_NAME_2+" ("
            +ORIGINAL_TITLE+" TEXT PRIMARY KEY , "
            +TITLE+" TEXT ,"
            +POSTER_PATH+" TEXT,"
            +OVERVIEW+" TEXT ,"
            +RELEASE_DATE+" TEXT,"
            +BACK_BATH+" TEXT ,"
            +VOTE+" TEXT ,"
            +MOVIE_ID+" TEXT )"
            ;

    private DBAdadpter (Context context){
        this.context = context;
        sqLiteDatabase = new SqlHelpter(this.context,DB_NAME,null,DB_VERSION).getWritableDatabase();

    }

    public static DBAdadpter getDBAdadpterInstance (Context context){
        if (dbAdadpter == null){
            dbAdadpter = new DBAdadpter(context);

        }
        return dbAdadpter;
    }

    public long Insert (String originaltitle,String title,String posterpath,String overview,
                           String Rdate,String Bpath,String vote,String id){
        ContentValues cv =new ContentValues();
        cv.put(ORIGINAL_TITLE,originaltitle);
        cv.put(TITLE,title);
        cv.put(POSTER_PATH,posterpath);
        cv.put(OVERVIEW,overview);
        cv.put(RELEASE_DATE,Rdate);
        cv.put(BACK_BATH,Bpath);
        cv.put(VOTE,vote);
        cv.put(MOVIE_ID,id);

        return sqLiteDatabase.insert(TABLE_NAME,null,cv) ;
    }

    public ArrayList<MovieModel> GetData (){
        ArrayList<MovieModel> movieModels = new ArrayList<>();
        String [] cols = new String[] {ORIGINAL_TITLE,TITLE,POSTER_PATH,OVERVIEW,RELEASE_DATE,BACK_BATH,VOTE,MOVIE_ID};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,cols,null,null,null,null,null);

        if(cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){

                MovieModel movieModel = new MovieModel();
                movieModel.setOriginal_title(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                movieModel.setPoster_path(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                movieModel.setRelease_date(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                movieModel.setBackdrop_path(cursor.getString(cursor.getColumnIndex(BACK_BATH)));
                movieModel.setVote_average(cursor.getString(cursor.getColumnIndex(VOTE)));
                movieModel.setId(cursor.getString(cursor.getColumnIndex(MOVIE_ID)));

                movieModels.add(movieModel);

            }

        }
         cursor.close();
        return movieModels;
    }

    public void DeleteAllDate (){
          sqLiteDatabase.execSQL("delete from " + TABLE_NAME);  ;
    }

    public long InsertFavMovie (String originaltitle,String title,String posterpath,String overview,
                        String Rdate,String Bpath,String vote,String id){
        ContentValues cv =new ContentValues();
        cv.put(ORIGINAL_TITLE,originaltitle);
        cv.put(TITLE,title);
        cv.put(POSTER_PATH,posterpath);
        cv.put(OVERVIEW,overview);
        cv.put(RELEASE_DATE,Rdate);
        cv.put(BACK_BATH,Bpath);
        cv.put(VOTE,vote);
        cv.put(MOVIE_ID,id);

        return sqLiteDatabase.insert(TABLE_NAME_2,null,cv) ;
    }

    public ArrayList<MovieModel> GetAllFavMovies (){
        ArrayList<MovieModel> movieModels = new ArrayList<>();
        String [] cols = new String[] {ORIGINAL_TITLE,TITLE,POSTER_PATH,OVERVIEW,RELEASE_DATE,BACK_BATH,VOTE,MOVIE_ID};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_2,cols,null,null,null,null,null);

        if(cursor != null && cursor.getCount() > 0){

            while (cursor.moveToNext()){

                MovieModel movieModel = new MovieModel();
                movieModel.setOriginal_title(cursor.getString(cursor.getColumnIndex(ORIGINAL_TITLE)));
                movieModel.setTitle(cursor.getString(cursor.getColumnIndex(TITLE)));
                movieModel.setPoster_path(cursor.getString(cursor.getColumnIndex(POSTER_PATH)));
                movieModel.setOverview(cursor.getString(cursor.getColumnIndex(OVERVIEW)));
                movieModel.setRelease_date(cursor.getString(cursor.getColumnIndex(RELEASE_DATE)));
                movieModel.setBackdrop_path(cursor.getString(cursor.getColumnIndex(BACK_BATH)));
                movieModel.setVote_average(cursor.getString(cursor.getColumnIndex(VOTE)));
                movieModel.setId(cursor.getString(cursor.getColumnIndex(MOVIE_ID)));

                movieModels.add(movieModel);

            }

        }
        cursor.close();
        return movieModels;
    }

    public boolean Checkid(String id){
        Cursor cursor=sqLiteDatabase.query(TABLE_NAME_2,new String []{MOVIE_ID},MOVIE_ID+" LIKE '%"+id+"%'",
                null,null,null,null,null);
        if (  cursor.getCount() > 0){
            return true;
        }

        return false;
    }

    public boolean DeleteMovie (String id){
        return sqLiteDatabase.delete(TABLE_NAME_2,MOVIE_ID+" = "+id,null) >0 ;
    }

    public Cursor getCursorsForAllData() {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_2
                ,new String []{ORIGINAL_TITLE,TITLE,POSTER_PATH,OVERVIEW,RELEASE_DATE,BACK_BATH,VOTE,MOVIE_ID},null,null,null,null,null,null);
        return cursor;
    }

    public Cursor getCursorForId(String id) {
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME_2,new String []{MOVIE_ID
                },MOVIE_ID+" LIKE '%"+id+"%'",null,null,null,null,null );
        return cursor;
    }

    public long InsertContentValue(ContentValues values) {
        return sqLiteDatabase.insert(TABLE_NAME_2,null,values);
    }

    public int deleteFav(String selection, String[] selectionArgs) {
        return sqLiteDatabase.delete(TABLE_NAME_2,selection,selectionArgs);
    }


    private static class SqlHelpter extends SQLiteOpenHelper{

        public SqlHelpter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_FAV_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
            onCreate(db);

        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
        }
    }
}
