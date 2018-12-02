package com.ionutlepi.movies.models


import com.squareup.moshi.Json
import java.io.Serializable
import java.util.Date


data class Movie(
    val id: Long,
    @field:Json(name = "release_date") val released: Date,
    @field:Json(name = "adult") val adult: Boolean,
    @field:Json(name = "vote_average") val rating: Double,
    @field:Json(name = "original_language") val language: String,
    @field:Json(name = "video") val hasTrailer: Boolean,
    @field:Json(name = "original_title") val originalTitle: String,
    @field:Json(name = "backdrop_path") override val backdropPath: String,
    @field:Json(name = "poster_path") override val posterPath: String,
    @field:Json(name = "overview") val description: String
) : Serializable, MovieCDNInterface