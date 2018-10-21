package com.ionutlepi.movies


import com.ionutlepi.movies.api.backDropHeightConfig
import com.ionutlepi.movies.api.cdnPath
import com.ionutlepi.movies.api.posterWidth
import com.squareup.moshi.Json
import java.io.Serializable


data class Movie(
    @field:Json(name = "original_title") val originalTitle: String,
    @field:Json(name = "backdrop_path") private val backdropPath: String,
    @field:Json(name = "poster_path") private val posterPath: String,
    @field:Json(name = "overview") val description: String
): Serializable {

    fun getMoviePosterUrl(): String = "$cdnPath/$posterWidth$posterPath"

    fun getBackDropUrl(): String = "$cdnPath/$backDropHeightConfig$backdropPath"
}