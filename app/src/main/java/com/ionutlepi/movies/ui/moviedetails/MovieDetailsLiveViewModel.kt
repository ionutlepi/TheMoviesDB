package com.ionutlepi.movies.ui.moviedetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ionutlepi.movies.api.MovieDbClientProvider
import com.ionutlepi.movies.api.TheMovieDB
import com.ionutlepi.movies.api.handleCallFailure
import com.ionutlepi.movies.api.handleResponseFailure
import com.ionutlepi.movies.models.MovieDetails
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsLiveViewModel(private val movieDbClient: TheMovieDB = MovieDbClientProvider.get()) :
    ViewModel() {
    val movieLiveData: MutableLiveData<MovieDetails> = MutableLiveData()

    fun load(id: Long) {
        val call = movieDbClient.movieDetails(id)
        call.enqueue(object : Callback<MovieDetails> {
            override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                handleCallFailure(call , t)
            }

            override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                if(response.isSuccessful) {
                    movieLiveData.postValue(response.body())
                } else {
                    handleResponseFailure(call, response)
                }
            }
        })
    }

}