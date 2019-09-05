package com.moetaz.popularmoviesapp.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.moetaz.popularmoviesapp.R
import com.moetaz.popularmoviesapp.models.Result
import kotlinx.android.synthetic.main.fragment_detail.*


class DetailFragment : Fragment() {
    lateinit var movie : Result
    private val BaseUrl = "http://image.tmdb.org/t/p/w185/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movie = activity!!.intent.getParcelableExtra<Result>("movie")
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
    }

    private fun initializeUI() {

        movieDesc.text = movie.overview
        Glide.with(this)
            .load("${BaseUrl}${movie.poster_path}").centerCrop().into(moviethumbnail)
        Glide.with(this)
            .load("${BaseUrl}${movie.poster_path}").into(moviePoster)
    }


}
