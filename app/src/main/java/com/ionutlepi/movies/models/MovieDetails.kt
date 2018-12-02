package com.ionutlepi.movies.models

import com.squareup.moshi.Json
import java.util.Date

data class MovieDetails(val id: Long,
                        @field:Json(name = "release_date") val released: Date,
                        @field:Json(name = "adult") val adult: Boolean,
                        @field:Json(name = "vote_average") val rating: Double,
                        @field:Json(name = "original_language") val language: String,
                        @field:Json(name = "video") val hasTrailer: Boolean,
                        @field:Json(name = "original_title") val originalTitle: String,
                        @field:Json(name = "backdrop_path") override val backdropPath: String,
                        @field:Json(name = "poster_path") override val posterPath: String,
                        @field:Json(name = "overview") val description: String,
                        val budget: Long,
                        val genres: List<MovieGenre>?,
                        val homepage: String,
                        @field:Json(name = "production_companies") val productionCompanies: List<ProductionCompany>,
                        @field:Json(name = "production_countries") val productionCountries: List<ProductionCountry>,
                        val revenue: Long,
                        val runtime: Int,
                        val status: String,
                        val tagline: String): MovieCDNInterface