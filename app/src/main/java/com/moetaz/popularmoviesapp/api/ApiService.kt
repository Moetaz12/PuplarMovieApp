package com.moetaz.popularmoviesapp.api

import com.moetaz.popularmoviesapp.models.Response
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apikey : String ,
                         @Query("page") page: Int) : Call<Response>

    @GET("movie/top_rated")
    fun getTopMovies(@Query("api_key") apikey : String ,
                         @Query("page") page: Int) : Call<Response>
}