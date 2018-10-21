package com.ionutlepi.movies


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.StringRes


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
