package com.example.moetaz.movieapp.parsing;

import com.example.moetaz.movieapp.models.MovieModel;
import com.example.moetaz.movieapp.models.ReviewModel;
import com.example.moetaz.movieapp.models.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Moetaz on 9/20/2017.
 */

public class Parse {
    public static ArrayList<MovieModel> parseStringToJson(String data) {
        ArrayList<MovieModel> movies = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray jsonArray = object.getJSONArray("results");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject finalobject = jsonArray.getJSONObject(i);
                MovieModel model = new MovieModel();

                model.setOriginal_title(finalobject.getString("original_title"));
                model.setTitle(finalobject.getString("title"));
                model.setPoster_path(finalobject.getString("poster_path"));
                model.setOverview(finalobject.getString("overview"));
                model.setRelease_date(finalobject.getString("release_date"));
                model.setBackdrop_path(finalobject.getString("backdrop_path"));
                model.setVote_average(finalobject.getString("vote_average"));
                model.setId(finalobject.getString("id"));

                movies.add(model);
            }

            return movies;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<ReviewModel> ParseReview (String data){
        ArrayList<ReviewModel> reviewModels = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("results");
            for(int i=0;i<array.length();i++){
                JSONObject finalObject=array.getJSONObject(i);
                ReviewModel model=new ReviewModel();
                model.setAuthor(finalObject.getString("author"));
                model.setContent(finalObject.getString("content"));
                model.setUrl(finalObject.getString("url"));
                reviewModels.add(model);
            }


            return reviewModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ArrayList<TrailerModel> ParseTrailer (String data){
        ArrayList<TrailerModel> trailerModels = new ArrayList<>();

        try {
            JSONObject object = new JSONObject(data);
            JSONArray array = object.getJSONArray("results");

            for(int i=0;i<array.length();i++){
                JSONObject FinalObject=array.getJSONObject(i);
                TrailerModel model=new TrailerModel();

                model.setKey(FinalObject.getString("key"));

                trailerModels.add(model);
            }

            return trailerModels;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
