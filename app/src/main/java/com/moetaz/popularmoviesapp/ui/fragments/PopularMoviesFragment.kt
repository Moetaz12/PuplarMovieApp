package com.moetaz.popularmoviesapp.ui.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.moetaz.popularmoviesapp.R
import com.moetaz.popularmoviesapp.adapters.MoviesAdapter
import com.moetaz.popularmoviesapp.models.Result
import com.moetaz.popularmoviesapp.ui.activities.DetailActivity
import com.moetaz.popularmoviesapp.utilities.Constants
import com.moetaz.popularmoviesapp.viewmodel.PopularMoviesViewModel
import kotlinx.android.synthetic.main.fragment_movies_list.*

class PopularMoviesFragment : Fragment() , MoviesAdapter.OnMovieClicked {

    override fun onClick(movie : Result) {
         startActivity(Intent(activity , DetailActivity::class.java)
             .putExtra("movie" , movie))
    }

    lateinit var viewModel : PopularMoviesViewModel
    var movies : ArrayList<Result> = ArrayList()
    lateinit var moviesAdapter : MoviesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_movies_list, container, false)
        return myView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        moviesRC.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel::class.java)
        moviesAdapter = MoviesAdapter(movies , context!!)
        moviesRC.adapter = moviesAdapter
        viewModel.getPopularMovies(Constants.API_KEY , 1)
            .observe(viewLifecycleOwner , Observer {
                 movies.addAll(it.results)
                 moviesAdapter.notifyDataSetChanged()
            })
        moviesAdapter.onMovieClicked = this
    }


}
