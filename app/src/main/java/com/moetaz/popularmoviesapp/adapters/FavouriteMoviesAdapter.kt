package com.moetaz.popularmoviesapp.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moetaz.popularmoviesapp.data.Movie
import com.moetaz.popularmoviesapp.utilities.Utile
import kotlinx.android.synthetic.main.movie_row.view.*


class FavouriteMoviesAdapter(val movies: ArrayList<Movie>, val context: Context) :
    RecyclerView.Adapter<FavouriteMoviesAdapter.MyViewHolder>() {

    private val BaseUrl = "http://image.tmdb.org/t/p/w185/"

    interface OnMovieClicked {
        fun onClick(movie: Movie)
        fun onFavClick(movie: Movie)
    }

    lateinit var onMovieClicked: OnMovieClicked
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context)
                .inflate(com.moetaz.popularmoviesapp.R.layout.movie_row, parent, false)
        )
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = movies.get(position)
        initializeUI(holder, movie)
        Log.d("fvmkdf","called")
        holder.itemView.setOnClickListener {
            onMovieClicked.onClick(movie)
        }

        holder.movieFav.setOnClickListener {
            onMovieClicked.onClick(movie)
        }

    }

    private fun initializeUI(holder: MyViewHolder, movie: Movie) {

        holder.title.text = movie.title
        holder.date.text = movie.date
        if (movie.overview.length < 100)
            holder.desc.text = movie.overview
        else
            holder.desc.text = "${movie.overview.substring(0, 100)} ..."
        holder.rate.text = movie.voteAverage

        Glide.with(context).load("$BaseUrl${movie.posterPath}").into(holder.posterImage)
        holder.lang.text = Utile.getLangiage(movie.language)
    }

    fun setItems(list: ArrayList<Movie>) {
        movies.clear()
        this.movies.addAll(list)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val title = view.movieTitle
        val posterImage = view.posterImage
        val date = view.date
        val desc = view.movieDesc
        val lang = view.lang
        val rate = view.movieRating
        val movieFav = view.movieFav

    }
}