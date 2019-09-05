package com.moetaz.popularmoviesapp.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.moetaz.popularmoviesapp.models.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object Factory {

        private var retrofit: Retrofit? = null
        private val Base_URL = "https://api.themoviedb.org/3/"

        @Synchronized
        fun getInstance() = retrofit ?: synchronized(this) {
            Retrofit.Builder().baseUrl(Base_URL).addConverterFactory(GsonConverterFactory.create())
                .build().also { retrofit = it }
        }

        fun getApiService() = getInstance().create(ApiService::class.java)

    }

    fun getPopularMovies(apikey: String, page: Int): LiveData<Response> {

        val responseLiveData = MutableLiveData<Response>()
        val apiServise = ApiClient.getApiService()
        val call = apiServise.getPopularMovies(apikey, page);
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                val apiResponse = response.body()
                responseLiveData.value = apiResponse

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("apiclient" , "error")
            }


        })

        return responseLiveData

    }

    fun getTopMovies(apikey: String, page: Int): LiveData<Response> {

        val responseLiveData = MutableLiveData<Response>()
        val apiServise = ApiClient.getApiService()
        val call = apiServise.getTopMovies(apikey, page);
        call.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {

                val apiResponse = response.body()
                responseLiveData.value = apiResponse

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.d("apiclient" , "error")
            }


        })

        return responseLiveData

    }
}