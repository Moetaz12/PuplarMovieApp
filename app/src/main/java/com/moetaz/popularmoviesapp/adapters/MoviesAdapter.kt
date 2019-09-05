package com.moetaz.popularmoviesapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.moetaz.popularmoviesapp.R
import com.moetaz.popularmoviesapp.models.Result
import kotlinx.android.synthetic.main.movie_row.view.*


class MoviesAdapter (val movies : List<Result> , val context : Context) :
    RecyclerView.Adapter<MoviesAdapter.MyViewHolder>()
{
    private val BaseUrl = "http://image.tmdb.org/t/p/w185/"
    interface OnMovieClicked{
        fun onClick(movie : Result)
    }
    lateinit var onMovieClicked : OnMovieClicked
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context)
            .inflate(R.layout.movie_row, parent, false))    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = movies.get(position)
        initializeUI(holder , movie)

        holder.itemView.setOnClickListener{
            onMovieClicked.onClick(movie)
        }

     }

    private fun initializeUI(holder: MyViewHolder, movie: Result) {

        holder.title.text = movie.original_title
        holder.date.text = movie.release_date
        if (movie.overview.length < 100)
        holder.desc.text = movie.overview
        else
            holder.desc.text = "${movie.overview.substring(0 , 100)} ..."
        holder.rate.text = movie.vote_average.toString()

        Glide.with(context).load("$BaseUrl${movie.poster_path}").into(holder.posterImage)
    }


    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val title = view.movieTitle
        val posterImage = view.posterImage
        val date = view.date
        val desc = view.movieDesc
        val duration = view.movieDuration
        val rate = view.movieRating

    }
}