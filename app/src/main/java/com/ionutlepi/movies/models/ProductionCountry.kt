package com.ionutlepi.movies.models

import com.squareup.moshi.Json

data class ProductionCountry(@field:Json(name = "iso_3166_1") val isoCode: String, val name: String)