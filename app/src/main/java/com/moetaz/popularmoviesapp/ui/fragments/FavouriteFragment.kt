package com.moetaz.popularmoviesapp.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.moetaz.popularmoviesapp.R
import com.moetaz.popularmoviesapp.adapters.FavouriteMoviesAdapter
import com.moetaz.popularmoviesapp.data.Movie
import com.moetaz.popularmoviesapp.viewmodel.FavouriteMoviesViewModel
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_movies_list.*

/**
 * A simple [Fragment] subclass.
 */
class FavouriteFragment : Fragment() , FavouriteMoviesAdapter.OnMovieClicked {
    override fun onFavClick(movie: Movie) {

    }

    override fun onClick(movie: Movie) {

    }

    private lateinit var favouriteMoviesViewModel: FavouriteMoviesViewModel
    var movies: ArrayList<Movie> = ArrayList()

    lateinit var moviesAdapter: FavouriteMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app_bar_title.text = "Favourite Movies"
        moviesRC.layoutManager = LinearLayoutManager(context)
        moviesAdapter = FavouriteMoviesAdapter(movies, context!!)
        moviesRC.adapter = moviesAdapter
        favouriteMoviesViewModel = ViewModelProviders.of(this).get(FavouriteMoviesViewModel::class.java)

        favouriteMoviesViewModel.allWords.observe(viewLifecycleOwner, Observer {
            movies.clear()
            movies.addAll(it)
            moviesAdapter.notifyDataSetChanged()
        })
        moviesAdapter.onMovieClicked = this
    }


}
