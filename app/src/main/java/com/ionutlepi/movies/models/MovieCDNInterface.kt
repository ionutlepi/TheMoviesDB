package com.ionutlepi.movies.models

import com.ionutlepi.movies.api.backDropHeightConfig
import com.ionutlepi.movies.api.cdnPath
import com.ionutlepi.movies.api.posterWidth

interface MovieCDNInterface {
    val posterPath: String
    val backdropPath: String

    fun getMoviePosterUrl(): String = "$cdnPath/$posterWidth$posterPath"

    fun getBackDropUrl(): String = "$cdnPath/$backDropHeightConfig$backdropPath"
}