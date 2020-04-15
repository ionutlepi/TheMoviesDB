package com.ionutlepi.movies


import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity


class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_movie_list)

    }

    fun setActionBarTitle(@StringRes id: Int) {
        supportActionBar?.setTitle(id)
    }
    fun setActionBarTitle(title: String) {
        supportActionBar?.title = title
    }
}
