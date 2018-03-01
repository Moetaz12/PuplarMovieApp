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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String MODEL_KEY = "DetailModel";
    @BindView(R.id.textTitle)
    TextView textViewtitle;
    @BindView(R.id.textOverview)
    TextView textViewOverviwe;
    @BindView(R.id.textrelaesDate)
    TextView textViewreleaseDate;
    @BindView(R.id.textVoteAverage)
    TextView textViewVoteAverage;
    @BindView(R.id.cardview1)
    CardView cardView1;
    @BindView(R.id.cardview2)
    CardView cardView2;
    @BindView(R.id.cardview3)
    CardView cardView3;
    @BindView(R.id.textreviwe1)
    TextView textViewReview1;
    @BindView(R.id.textreviwe2)
    TextView textViewReview2;
    @BindView(R.id.textreviwe3)
    TextView textViewReview3;
    @BindView(R.id.auther1)
    TextView textViewAuther1;
    @BindView(R.id.auther2)
    TextView textViewAuther2;
    @BindView(R.id.auther3)
    TextView textViewAuther3;
    @BindView(R.id.texturl1)
    TextView textViewurl1;
    @BindView(R.id.texturl2)
    TextView textViewurl2;
    @BindView(R.id.texturl3)
    TextView textViewurl3;
    @BindView(R.id.imageAuther1)
    ImageView imageViewauther1;
    @BindView(R.id.imageAuther2)
    ImageView imageViewauther2;
    @BindView(R.id.imageAuther3)
    ImageView imageViewauther3;
    @BindView(R.id.image_detail)
    ImageView imageView;
    @BindView(R.id.favoriteButton)
    ImageButton imageButton;
    public boolean FirstTimeLoad = false;
    public boolean FirstTimeLoad1 = false;
    private ContentResolver contentResolver;
    private final String BaseUrl = "http://image.tmdb.org/t/p/w185/";
    private String imgurl, movieID;
    private MovieModel movie;
    Unbinder unbind;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MODEL_KEY, movie);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentResolver = getActivity().getContentResolver();
        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(MODEL_KEY);
        } else {

            Intent intent = getActivity().getIntent();
            movie = intent.getParcelableExtra("modelPass");
            if (movie == null)
                movie = (MovieModel) getArguments().getSerializable("modelPass");
        }

        assert movie != null;
        imgurl = movie.getPoster_path();
        movieID = movie.getId();
        if (!FirstTimeLoad) {
            FirstTimeLoad = true;
            getLoaderManager().initLoader(2, null, this);
        } else {
            getLoaderManager().restartLoader(2, null, this);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootview = inflater.inflate(R.layout.fragment_detail, container, false);
        unbind = ButterKnife.bind(this, rootview);
        textViewtitle.setText(movie.getOriginal_title());
        textViewOverviwe.setText(movie.getOverview());
        textViewreleaseDate.setText(movie.getRelease_date());
        textViewVoteAverage.setText(movie.getVote_average());
        loadImage();
        LoadReviews(movieID);
        LoadTrailers(movieID);
        imageButton.setOnClickListener(this);

        return rootview;
    }

    public void InsertByResolver() {
        ContentValues cv = new ContentValues();
        cv.put(ORIGINAL_TITLE, movie.getOriginal_title());
        cv.put(TITLE, movie.getTitle());
        cv.put(POSTER_PATH, movie.getPoster_path());
        cv.put(OVERVIEW, movie.getOverview());
        cv.put(RELEASE_DATE, movie.getRelease_date());
        cv.put(BACK_BATH, movie.getBackdrop_path());
        cv.put(MOVIE_ID, movieID);
        cv.put(VOTE, movie.getVote_average());
        contentResolver.insert(CONTENT_URI_1, cv);
    }

    public void DeleteByResolver(String id) {
        contentResolver.delete(CONTENT_URI_1
                , MOVIE_ID + " = ?", new String[]{id});
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    public void LoadReviews(String id) {
        final ArrayList<ReviewModel> reviewModels = new ArrayList<>();

        String baseUrl = "http://api.themoviedb.org/3/movie/" + id + "/reviews?";
        String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
        String lasturl = baseUrl.concat(apiKey);
        Cache cache = Mysingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(lasturl);
        if (entry != null) {
            try {
                String data = new String(entry.data, "UTF-8");

                Iterator iterator = Parse.ParseReview(data).iterator();
                while (iterator.hasNext()) {
                    ReviewModel reviewModel = (ReviewModel) iterator.next();
                    reviewModels.add(reviewModel);

                }

                fillReview(reviewModels);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, lasturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Iterator iterator = Parse.ParseReview(response).iterator();
                    while (iterator.hasNext()) {

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

    public void LoadTrailers(String id) {
        final ArrayList<TrailerModel> trailerModels = new ArrayList<>();

        String baseUrl = "http://api.themoviedb.org/3/movie/" + id + "/videos?";
        String apiKey = "api_key=" + BuildConfig.MOVIE_APP_API_KEY;
        String lasturl = baseUrl.concat(apiKey);

        Cache cache = Mysingleton.getInstance(getActivity()).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(lasturl);
        if (entry != null) {
            Toast.makeText(getContext(), "in", Toast.LENGTH_SHORT).show();
            try {
                String data = new String(entry.data, "UTF-8");

                Iterator iterator = Parse.ParseTrailer(data).iterator();
                while (iterator.hasNext()) {
                    TrailerModel trailerModel = (TrailerModel) iterator.next();
                    trailerModels.add(trailerModel);

                }

                fillTrailers(trailerModels);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, lasturl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Iterator iterator = Parse.ParseTrailer(response).iterator();
                    while (iterator.hasNext()) {

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

    private void fillReview(final ArrayList<ReviewModel> Reviwelist) {
        int size = Reviwelist.size();
        if (size >= 1) {
            textViewReview1.setText(Reviwelist.get(0).getContent());
            textViewAuther1.setText(Reviwelist.get(0).getAuthor());
            textViewurl1.setText(Reviwelist.get(0).getUrl());
            textViewurl1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToLink(Reviwelist.get(0).getUrl());
                }
            });
        } else {
            imageViewauther1.setVisibility(View.GONE);
            textViewReview1.setVisibility(View.GONE);
            textViewurl1.setVisibility(View.GONE);
        }

        if (size >= 2) {
            textViewReview2.setText(Reviwelist.get(1).getContent());
            textViewAuther2.setText(Reviwelist.get(1).getAuthor());
            textViewurl2.setText(Reviwelist.get(1).getUrl());

            textViewurl2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToLink(Reviwelist.get(1).getUrl());
                }
            });
        } else {
            imageViewauther2.setVisibility(View.GONE);
            textViewReview2.setVisibility(View.GONE);
            textViewurl2.setVisibility(View.GONE);
        }


        if (size >= 3) {
            textViewReview3.setText(Reviwelist.get(2).getContent());
            textViewAuther3.setText(Reviwelist.get(2).getAuthor());
            textViewurl3.setText(Reviwelist.get(2).getUrl());

            textViewurl3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GoToLink(Reviwelist.get(2).getUrl());
                }
            });
        } else {
            imageViewauther3.setVisibility(View.GONE);
            textViewReview3.setVisibility(View.GONE);
            textViewurl3.setVisibility(View.GONE);
        }
    }

    private void GoToLink(String Rurl) {
        Intent intent = null, chooser = null;

        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(Rurl));

        chooser = Intent.createChooser(intent, "open...");
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivity(chooser);

        }
    }

    private void fillTrailers(final ArrayList<TrailerModel> Trailerlist) {
        int size = Trailerlist.size();

        if (size >= 1) {
            cardView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchTrailers(Trailerlist.get(0).getKey());
                }
            });
        } else {
            cardView1.setVisibility(View.GONE);
        }
        if (size >= 2) {
            cardView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchTrailers(Trailerlist.get(1).getKey());
                }
            });

        } else {
            cardView2.setVisibility(View.GONE);
        }

        if (size >= 3) {
            cardView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WatchTrailers(Trailerlist.get(2).getKey());
                }
            });
        } else {
            cardView3.setVisibility(View.GONE);
        }
    }

    private void WatchTrailers(String id) {
        Intent appintent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appintent);
        } catch (ActivityNotFoundException e) {
            startActivity(webintent);
        }

    }

    private void loadImage() {
        if (isNetworkConnected(getActivity()))
            Picasso.with(getActivity()).load(BaseUrl + imgurl).into(imageView);
        else
            Picasso.with(getActivity()).load(BaseUrl + imgurl)
                    .networkPolicy(NetworkPolicy.OFFLINE).into(imageView);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (id == 2) {
            return new CursorLoader(getContext(), CONTENT_URI_2, null, null, new String[]{movieID}, null);
        } else if (id == 3) {
            return new CursorLoader(getContext(), CONTENT_URI_2, null, null, new String[]{movieID}, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (loader.getId() == 2) {
            if (cursor.getCount() > 0) {

                imageButton.setImageResource(R.drawable.button_pressed);
            } else {

                imageButton.setImageResource(R.drawable.button_normal);
            }
            cursor.close();
        } else if (loader.getId() == 3) {
            if (cursor.getCount() > 0) {
                imageButton.setImageResource(R.drawable.button_normal);
                DeleteByResolver(movieID);
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
        switch (v.getId()) {
            case R.id.favoriteButton:
                if (!FirstTimeLoad1) {
                    FirstTimeLoad1 = true;
                    getLoaderManager().initLoader(3, null, this);
                } else {
                    getLoaderManager().restartLoader(3, null, this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbind.unbind();
    }
}
