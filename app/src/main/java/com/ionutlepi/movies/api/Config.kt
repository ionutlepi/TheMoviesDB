package com.ionutlepi.movies.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

import okhttp3.OkHttpClient


const val apiProtocol = "https"
const val apiUrl = "api.themoviedb.org"
const val backDropHeightConfig = "w533_and_h300_bestv2"
const val posterWidth =  "w185"
const val cdnPath = "https://image.tmdb.org/t/p"
const val apiVersion = "3"
//for safe keeping this should be normally downloaded from site so we can have it guarded
const val apiKey = "???????????????????"

val httpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor { chain ->
    val original = chain.request()
    val originalHttpUrl = original.url()

    val url = originalHttpUrl.newBuilder()
        .addQueryParameter("api_key", apiKey)
        .build()

    // Request customization: add request headers
    val requestBuilder = original.newBuilder()
        .url(url)

    val request = requestBuilder.build()
    chain.proceed(request)
}.build()

val movieDbRetrofitProvider: Retrofit = Retrofit.Builder()
    .baseUrl("$apiProtocol://$apiUrl/$apiVersion/")
    .addConverterFactory(MoshiConverterFactory.create())
    .client(httpClient)
    .build()

val movieDbClient = movieDbRetrofitProvider.create(TheMovieDB::class.java)