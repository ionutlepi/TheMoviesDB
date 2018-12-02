package com.ionutlepi.movies.models

import com.squareup.moshi.Json

data class ProductionCompany(val id: Long,
                             @field:Json(name = "logo_path") private val logoPath: String,
                             val name: String,
                             @field:Json(name = "origin_country") val country: String)