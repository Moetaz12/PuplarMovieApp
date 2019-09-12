package com.moetaz.popularmoviesapp.data

import androidx.lifecycle.LiveData

class MovieRepository(private val movieDao: MovieDao) {

    val allMovie : LiveData<List<Movie>> = movieDao.getAllMovies()

    suspend fun insert(movie: Movie){
        movieDao.insert(movie)
    }

    suspend fun deleteMovie(id: String){
        movieDao.deleteMovie(id)
    }

    fun getMovieById(id : String): LiveData<List<Movie>>{
        return movieDao.getMovieById(id)
    }

}