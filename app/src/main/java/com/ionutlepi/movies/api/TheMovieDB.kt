package com.ionutlepi.movies.api

import com.ionutlepi.movies.models.MovieDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

import retrofit2.http.Query

interface TheMovieDB {

    @GET("movie/now_playing")
    fun nowPlaying(@Query("page") page: Int = 1): Call<ApiMovieList>


    @GET("movie/{id}")
    fun movieDetails(@Path("id")  id: Long): Call<MovieDetails>

    @GET("search/movie")
    fun search(@Query("query") query: String, @Query("page") page: Int = 1): Call<ApiMovieList>
}