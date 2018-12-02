package com.ionutlepi.movies

import java.text.DateFormat
import java.util.Date

fun Date.formmatForDisplay(): String {
    val df = DateFormat.getDateInstance()
    return df.format(this)
}