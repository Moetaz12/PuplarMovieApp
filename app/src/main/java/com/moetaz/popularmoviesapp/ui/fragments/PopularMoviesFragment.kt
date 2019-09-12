package com.moetaz.popularmoviesapp.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.moetaz.popularmoviesapp.R
import com.moetaz.popularmoviesapp.adapters.MoviesAdapter
import com.moetaz.popularmoviesapp.models.Movie
import com.moetaz.popularmoviesapp.ui.activities.DetailActivity
import com.moetaz.popularmoviesapp.utilities.Constants
import com.moetaz.popularmoviesapp.viewmodel.FavouriteMoviesViewModel
import com.moetaz.popularmoviesapp.viewmodel.PopularMoviesViewModel
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_movies_list.*

class PopularMoviesFragment : Fragment(), MoviesAdapter.OnMovieClicked,
    MoviesAdapter.OnMovieLoaded {

    var favMovies: ArrayList<com.moetaz.popularmoviesapp.data.Movie> = ArrayList()
    override fun onLoaded(favIcon: ImageView, movie: Movie) {
        /*if (favouriteMoviesViewModel.getMovieById(movie.id.toString()).value!!.size > 0){
            favIcon.setBackgroundResource(R.drawable.ic_fav)
        }else{
            favIcon.setBackgroundResource(R.drawable.ic_unfav)
        }*/

        favouriteMoviesViewModel.getMovieById(movie.id.toString())
            .observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    favIcon.setTag("fav")
                    favIcon.setBackgroundResource(R.drawable.ic_fav)
                } else {
                    favIcon.setTag("unfav")
                    favIcon.setBackgroundResource(R.drawable.ic_unfav)
                }
            })
    }


    override fun onFavClick(favIcon : ImageView ,movie: Movie) {
        if (favIcon.tag.equals("unfav")) {
            favIcon.tag = "fav"
            favouriteMoviesViewModel.insert(
                com.moetaz.popularmoviesapp.data.Movie(
                    movie.id.toString(),
                    movie.original_language,
                    movie.overview,
                    movie.poster_path,
                    movie.release_date,
                    movie.original_title,
                    movie.vote_average.toString()
                )
            )
        }else{
            favIcon.tag = "unfav"
            favouriteMoviesViewModel.deleteMovie(movie.id.toString())
        }
    }

    override fun onClick(movie: Movie) {
        startActivity(
            Intent(activity, DetailActivity::class.java)
                .putExtra("movie", movie)
        )

    }

    //viewmodels
    lateinit var viewModel: PopularMoviesViewModel
    lateinit var favouriteMoviesViewModel: FavouriteMoviesViewModel

    var movies: ArrayList<Movie> = ArrayList()
    var movies2: ArrayList<Movie> = ArrayList()

    lateinit var moviesAdapter: MoviesAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val myView = inflater.inflate(R.layout.fragment_movies_list, container, false)
        favouriteMoviesViewModel =
            ViewModelProviders.of(this).get(FavouriteMoviesViewModel::class.java)

        return myView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        app_bar_title.text = "Popular Movies"
        moviesRC.layoutManager = LinearLayoutManager(context)
        viewModel = ViewModelProviders.of(this).get(PopularMoviesViewModel::class.java)
        moviesAdapter = MoviesAdapter(movies, context!!)
        moviesRC.adapter = moviesAdapter
        viewModel.getPopularMovies(Constants.API_KEY, 1)
            .observe(viewLifecycleOwner, Observer {
                movies.clear()
                movies.addAll(it.results)
                movies2.clear()
                movies2.addAll(it.results)
                moviesAdapter.notifyDataSetChanged()
            })
        moviesAdapter.onMovieClicked = this
        moviesAdapter.onMovieLoaded = this
        imageView_search.setOnClickListener {
            backBtn.visibility = View.VISIBLE
            app_bar_title.visibility = View.INVISIBLE
            imageView_search.visibility = View.INVISIBLE
            edittext_search.visibility = View.VISIBLE
        }

        backBtn.setOnClickListener {
            backBtn.visibility = View.INVISIBLE
            app_bar_title.visibility = View.VISIBLE
            imageView_search.visibility = View.VISIBLE
            edittext_search.visibility = View.INVISIBLE
        }

        edittext_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d("nvlnf before", "${movies.size}")
                /* var listTemp : ArrayList<Movie> = ArrayList();
                 listTemp.addAll(movies)

                 movies.clear()
                 moviesAdapter.notifyDataSetChanged()
                 movies.addAll(getSerachResult(listTemp, "$s"))

                 moviesAdapter.notifyDataSetChanged()

                 movies.clear()
                 movies.addAll(listTemp)
                 listTemp.clear()*/
                movies.clear()
                movies.addAll(movies2)
                moviesAdapter.setItems(getSerachResult(movies, "$s"))
                moviesAdapter.notifyDataSetChanged()
                Log.d("nvlnf after", "${movies.size}")

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


            }
        })

    }

    private fun getSerachResult(list: ArrayList<Movie>, key: String): ArrayList<Movie> {
        var temp: ArrayList<Movie> = ArrayList()
        for (movie in list) {
            if (movie.title.toLowerCase().contains(key))
                temp.add(movie)
        }
        return temp
    }

}
