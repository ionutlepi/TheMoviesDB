package com.ionutlepi.movies.api

import com.ionutlepi.movies.Movie
import com.squareup.moshi.Json


data class ApiMovieList(
    val results: List<Movie>,
    val page: Int,
    @field:Json(name = "total_results") val totalResults: Int,
    @field:Json(name = "total_pages") val totalPages: Int
)