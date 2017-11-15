package com.example.moetaz.movieapp.interfaces;

import android.net.Uri;

/**
 * Created by Moetaz on 9/24/2017.
 */

public interface MoviesProivderConstants {

    Uri CONTENT_URI_1 = Uri.parse
            ("content://com.example.moetaz.movieapp/MOVIES_LIST");
    Uri CONTENT_URI_2 = Uri.parse
            ("content://com.example.moetaz.movieapp/MOVIE_ID");

    public String ORIGINAL_TITLE = "original_title";
    public String TITLE = "title";
    public String POSTER_PATH ="poster_path";
    public String OVERVIEW = "overview";
    public String RELEASE_DATE = "release_date";
    public String BACK_BATH = "backdrop_path";
    public String VOTE = "vote_average";
    public String MOVIE_ID = "mid";
}
