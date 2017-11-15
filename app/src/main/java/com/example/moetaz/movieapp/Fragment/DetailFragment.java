package com.example.moetaz.movieapp.fragment;


import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.moetaz.movieapp.BuildConfig;
import com.example.moetaz.movieapp.R;
import com.example.moetaz.movieapp.models.MovieModel;
import com.example.moetaz.movieapp.models.ReviewModel;
import com.example.moetaz.movieapp.models.TrailerModel;
import com.example.moetaz.movieapp.parsing.Parse;
import com.example.moetaz.movieapp.utilities.Mysingleton;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.BACK_BATH;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.CONTENT_URI_1;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.CONTENT_URI_2;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.MOVIE_ID;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.ORIGINAL_TITLE;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.OVERVIEW;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.POSTER_PATH;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.RELEASE_DATE;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.TITLE;
import static com.example.moetaz.movieapp.interfaces.MoviesProivderConstants.VOTE;
import static com.example.moetaz.movieapp.utilities.MyUtilities.isNetworkConnected;
import static com.example.moetaz.movieapp.utilities.MyUtilities.message;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public boolean FirstTimeLoad = false;
    public boolean FirstTimeLoad1 = false;
    public boolean IsMarkedAsFav = false;
    private ContentResolver contentResolver;

    private final String BaseUrl = "http://image.tmdb.org/t/p/w185/";
    private ImageView imageView;
    private String imgurl;
    private String MovieID;

    private MovieModel movie;
    private CardView cardView1,cardView2,cardView3;
    private TextView textViewtitle,textViewOverviwe,textViewreleaseDate,textViewVoteAverage
             ,textViewReview1,textViewReview2,textViewReview3
             ,textViewAuther1,textViewAuther2,textViewAuther3
             ,textViewurl1,textViewurl2,textViewurl3;
    private ImageView imageViewauther1,imageViewauther2,imageViewauther3;
    private ImageButton imageButton;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DetailModel",movie);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            movie = (MovieModel) savedInstanceState.getSerializable("DetailModel");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
        if(savedInstanceState != null){
            message(getActivity(),"savedInstanceState Not null");
            movie = (MovieModel) savedInstanceState.getSerializable("DetailModel");
        }else{
            message(getActivity(),"savedInstanceState null");
            Intent intent = getActivity().getIntent();
            movie = (MovieModel) intent.getSerializableExtra("modelPass");
            if(movie == null)
                movie = (MovieModel) getArguments().getSerializable("modelPass");
        }


        imgurl= movie.getPoster_path();
        MovieID = movie.getId();
        if (!FirstTimeLoad){
            FirstTimeLoad = true;
            getLoaderManager().initLoader(2,null,this);}
        else{
            getLoaderManager().restartLoader(2,null,this );
        }
    } //inc@t!ec

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview=inflater.inflate(R.layout.fragment_detail, container, false);

        imageView= (ImageView)rootview.findViewById(R.id.image_detail);
        textViewtitle= (TextView) rootview.findViewById(R.id.textTitle);
        textViewOverviwe= (TextView) rootview.findViewById(R.id.textOverview);
        textViewreleaseDate= (TextView) rootview.findViewById(R.id.textrelaesDate);
        textViewVoteAverage= (TextView) rootview.findViewById(R.id.textVoteAverage);
        cardView1= (CardView) rootview.findViewById(R.id.cardview1);
        cardView2= (CardView) rootview.findViewById(R.id.cardview2);
        cardView3= (CardView) rootview.findViewById(R.id.cardview3);
        textViewReview1= (TextView) rootview.findViewById(R.id.textreviwe1);
        textViewReview2= (TextView) rootview.findViewById(R.id.textreviwe2);
        textViewReview3= (TextView) rootview.findViewById(R.id.textreviwe3);
        textViewAuther1= (TextView) rootview.findViewById(R.id.auther1);
        textViewAuther2= (TextView) rootview.findViewById(R.id.auther2);
        textViewAuther3= (TextView) rootview.findViewById(R.id.auther3);
        imageViewauther1= (ImageView) rootview.findViewById(R.id.imageAuther1);
        imageViewauther2= (ImageView) rootview.findViewById(R.id.imageAuther2);
        imageViewauther3= (ImageView) rootview.findViewById(R.id.imageAuther3);
        textViewurl1= (TextView) rootview.findViewById(R.id.texturl1);
        textViewurl2= (TextView) rootview.findViewById(R.id.texturl2);
        textViewurl3= (TextView) rootview.findViewById(R.id.texturl3);
        imageButton= (ImageButton) rootview.findViewById(R.id.favoriteButton);

        Loadimg();

        textViewtitle.setText(movie.getOriginal_title());
        textViewOverviwe.setText(movie.getOverview());
        textViewreleaseDate.setText(movie.getRelease_date());
        textViewVoteAverage.setText(movie.getVote_average());

        LoadReviewByVolley(MovieID);
        LoadTrailerByVolley(MovieID);

        imageButton.setOnClickListener(this);

        return rootview;
    }

    public void InsertByResolver(){
        ContentValues cv = new ContentValues();
        cv.put(ORIGINAL_TITLE,movie.getOriginal_title());
        cv.put(TITLE,movie.getTitle());
        cv.put(POSTER_PATH,movie.getPoster_path());
        cv.put(OVERVIEW,movie.getOverview());
        cv.put(RELEASE_DATE,movie.getRelease_date());
        cv.put(BACK_BATH,movie.getBackdrop_path());
        cv.put(MOVIE_ID, MovieID);
        cv.put(VOTE,movie.getVote_average());
        contentResolver.insert(CONTENT_URI_1,cv);
    }

    public void DeleteByResolver(String id){
        contentResolver.delete(CONTENT_URI_1
                ,MOVIE_ID +" = ?",new String[]{id});
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void LoadReviewByVolley (String id){
        final ArrayList<ReviewModel> reviewModels = new ArrayList<>();

        String baseUrl = "http://api.themoviedb.org/3/movie/"+id+"/reviews?";
        String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
        String lasturl = baseUrl.concat(apiKey);
        Cache cache = Mysingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(lasturl);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");

                Iterator iterator = Parse.ParseReview(data).iterator();
                while (iterator.hasNext()){
                    ReviewModel reviewModel = (ReviewModel) iterator.next();
                    reviewModels.add(reviewModel);

                }

                fillReview(reviewModels);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, lasturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Iterator iterator = Parse.ParseReview(response).iterator();
                    while (iterator.hasNext()){

                        ReviewModel reviewModel = (ReviewModel) iterator.next();
                        reviewModels.add(reviewModel);

                    }
                    fillReview(reviewModels);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            Mysingleton.getInstance(getActivity()).addToRequest(stringRequest);

        }
    }

    public void LoadTrailerByVolley (String id){
        final ArrayList<TrailerModel> trailerModels = new ArrayList<>();

        String baseUrl = "http://api.themoviedb.org/3/movie/"+id+"/videos?";
        String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
        String lasturl = baseUrl.concat(apiKey);

        Cache cache = Mysingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(lasturl);
        if (entry != null) {
            Toast.makeText(getContext(),"in",Toast.LENGTH_SHORT).show();
            try {
                String data = new String(entry.data, "UTF-8");

                Iterator iterator = Parse.ParseTrailer(data).iterator();
                while (iterator.hasNext()){
                    TrailerModel trailerModel = (TrailerModel) iterator.next();
                    trailerModels.add(trailerModel);

                }

                fillTrailers(trailerModels);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }else {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, lasturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Iterator iterator = Parse.ParseTrailer(response).iterator();
                    while (iterator.hasNext()){

                        TrailerModel trailerModel = (TrailerModel) iterator.next();
                        trailerModels.add(trailerModel);

                    }
                    fillTrailers(trailerModels);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            Mysingleton.getInstance(getActivity()).addToRequest(stringRequest);

        }
    }

    private void fillReview(final ArrayList<ReviewModel> Reviwelist){
        int size=Reviwelist.size();
        if(size>=1){
            textViewReview1.setText(Reviwelist.get(0).getContent());
            textViewAuther1.setText(Reviwelist.get(0).getAuthor());
            textViewurl1.setText(Reviwelist.get(0).getUrl());
            textViewurl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToLink(Reviwelist.get(0).getUrl());
                }
            });
        }
        else {
            imageViewauther1.setVisibility(View.GONE);
            textViewReview1.setVisibility(View.GONE);
            textViewurl1.setVisibility(View.GONE);
        }

        if(size>=2){
            textViewReview2.setText(Reviwelist.get(1).getContent());
            textViewAuther2.setText(Reviwelist.get(1).getAuthor());
            textViewurl2.setText(Reviwelist.get(1).getUrl());

            textViewurl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToLink(Reviwelist.get(1).getUrl());
                }
            });
        }
        else {
            imageViewauther2.setVisibility(View.GONE);
            textViewReview2.setVisibility(View.GONE);
            textViewurl2.setVisibility(View.GONE);
        }


        if(size>=3) {
            textViewReview3.setText(Reviwelist.get(2).getContent());
            textViewAuther3.setText(Reviwelist.get(2).getAuthor());
            textViewurl3.setText(Reviwelist.get(2).getUrl());

            textViewurl3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToLink(Reviwelist.get(2).getUrl());
                }
            });
        }
        else{
            imageViewauther3.setVisibility(View.GONE);
            textViewReview3.setVisibility(View.GONE);
            textViewurl3.setVisibility(View.GONE);
        }
    }

    private void GoToLink(String Rurl){
        Intent intent=null, chooser=null;

        intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Rurl));

        chooser=Intent.createChooser(intent,"open...");
        if(intent.resolveActivity(getContext().getPackageManager())!=null){
            startActivity(chooser);

        }
    }

    private void fillTrailers (final ArrayList<TrailerModel> Trailerlist){
        int size=Trailerlist.size();

        if(size>=1){
            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchTrailers(Trailerlist.get(0).getKey());
                }
            });
        }else {
            cardView1.setVisibility(View.GONE);
        }
        if(size>=2){
            cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchTrailers(Trailerlist.get(1).getKey());
                }
            });

        }else {
            cardView2.setVisibility(View.GONE);
        }

        if (size>=3){
            cardView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchTrailers(Trailerlist.get(2).getKey());
                }
            });
        }else {
            cardView3.setVisibility(View.GONE);
        }
    }

    private void WatchTrailers(String id){
        Intent appintent=new Intent(Intent.ACTION_VIEW,Uri.parse("vnd.youtube:"+id));
        Intent webintent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.youtube.com/watch?v="+id));
        try {
            startActivity(appintent);
        }catch (ActivityNotFoundException e){
            startActivity(webintent);
        }

    }

    private void Loadimg(){
        if(isNetworkConnected(getActivity()))
        Picasso.with(getActivity()).load(BaseUrl+imgurl).into(imageView);
        else
            Picasso.with(getActivity()).load(BaseUrl+imgurl)
                    .networkPolicy(NetworkPolicy.OFFLINE).into(imageView);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if(id == 2){
            return new CursorLoader(getContext(),CONTENT_URI_2,null,null,new String []{MovieID},null);
        }else if(id == 3){
            return new CursorLoader(getContext(),CONTENT_URI_2,null,null,new String []{MovieID},null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(loader.getId() == 2) {
            if (cursor.getCount() > 0) {

                imageButton.setImageResource(R.drawable.button_pressed);
            } else {

                imageButton.setImageResource(R.drawable.button_normal);
            }
            cursor.close();
        }
        else if (loader.getId() == 3){
            if (cursor.getCount() > 0) {
                imageButton.setImageResource(R.drawable.button_normal);
                DeleteByResolver(MovieID);
            } else {
                imageButton.setImageResource(R.drawable.button_pressed);
                InsertByResolver();
            }
            cursor.close();
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.favoriteButton:
                if (!FirstTimeLoad1) {
                    FirstTimeLoad1 = true;
                    getLoaderManager().initLoader(3,null,this);
                }
                else{
                    getLoaderManager().restartLoader(3,null,this );
                }
                break;
            default:break;
        }
    }
}
