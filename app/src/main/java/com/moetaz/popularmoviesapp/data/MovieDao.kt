package com.moetaz.popularmoviesapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Query("SELECT * from favourite_movies ORDER BY id ASC")
    fun getAllMovies(): LiveData<List<Movie>>

    @Query("SELECT * from favourite_movies WHERE id = :mid ")
    fun getMovieById(mid :String): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Query("DELETE FROM favourite_movies")
    suspend fun deleteAll()

    @Query("DELETE FROM favourite_movies WHERE id = :mid")
    suspend fun deleteMovie(mid :String)

}