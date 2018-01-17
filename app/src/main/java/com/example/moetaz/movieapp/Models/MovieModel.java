package com.example.moetaz.movieapp.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


/**
 * Created by Moetaz on 10/29/2016.
 */

public class MovieModel implements Parcelable {
    private String title;
    private String original_title;
    private String poster_path;
    private String overview;
    private String release_date;
    private String backdrop_path;
    private String vote_average;
    private String id;


    public MovieModel(){

    }
    protected MovieModel(Parcel in) {
        title = in.readString();
        original_title = in.readString();
        poster_path = in.readString();
        overview = in.readString();
        release_date = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readString();
        id = in.readString();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getOverview() {

        return overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(original_title);
        dest.writeString(poster_path);
        dest.writeString(overview);
        dest.writeString(release_date);
        dest.writeString(backdrop_path);
        dest.writeString(vote_average);
        dest.writeString(id);
    }
}
