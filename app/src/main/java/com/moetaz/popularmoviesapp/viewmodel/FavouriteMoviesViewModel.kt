package com.moetaz.popularmoviesapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.moetaz.popularmoviesapp.data.Movie
import com.moetaz.popularmoviesapp.data.MovieRepository
import com.moetaz.popularmoviesapp.data.MovieRoomDatabase
import kotlinx.coroutines.launch

class FavouriteMoviesViewModel (application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: MovieRepository
    // LiveData gives us updated words when they change.
    val allWords: LiveData<List<Movie>>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        val wordsDao = MovieRoomDatabase.getDatabase(application).wordDao()
        repository = MovieRepository(wordsDao)
        allWords = repository.allMovie
    }

    // The implementation of insert() is completely hidden from the UI.
    // We don't want insert to block the main thread, so we're launching a new
    // coroutine. ViewModels have a coroutine scope based on their lifecycle called
    // viewModelScope which we can use here.
    fun insert(movie: Movie) = viewModelScope.launch {
        repository.insert(movie)
    }

    fun deleteMovie(id : String) = viewModelScope.launch {
        repository.deleteMovie(id)
    }

    fun getMovieById (id : String) :LiveData<List<Movie>>{
        return repository.getMovieById(id)
    }
}