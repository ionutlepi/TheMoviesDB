package com.ionutlepi.movies.ui.movielist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.ionutlepi.movies.api.ApiMovieList
import com.ionutlepi.movies.api.MovieDbClientProvider
import com.ionutlepi.movies.api.TheMovieDB
import com.ionutlepi.movies.models.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.atomic.AtomicBoolean

class MovieListViewModel(private val movieDbClient: TheMovieDB = MovieDbClientProvider.get()) :
    ViewModel() {
    private var movieList: MutableLiveData<List<Movie>>? = null
    var pagingMovieList: MutableLiveData<List<Movie>> = MutableLiveData()
    var currentPage = 1
    private val loadingData: AtomicBoolean = AtomicBoolean(false)
    private var searchCallReference: Call<ApiMovieList>? = null
    private var activeQuery: String? = null

    private val apiCB = object: Callback<ApiMovieList>{
        override fun onFailure(call: Call<ApiMovieList>, t: Throwable) {
            Log.w("MovieListViewModel", t)
            loadingData.set(false)
        }

        override fun onResponse(call: Call<ApiMovieList>, response: Response<ApiMovieList>) {
            val body = response.body()
            if(response.isSuccessful && body != null) {
                currentPage = 1
                movieList?.postValue(body.results)
            } else {
                Log.w("MovieListViewModel", response.message())
            }
        }

    }

    fun getMovies(): LiveData<List<Movie>> {
        if(movieList == null) {
            movieList = MutableLiveData()
            loadMovies()
        }
        return movieList!!
    }

    /**
     * Do a search for the given [query]. If called before the current search completes it cancels
     * the current one
     * @param query
     */
    fun search(query: String) {
        searchCallReference?.cancel() //cancel the old call so we don't bombard the api
        searchCallReference = movieDbClient.search(query)
        activeQuery = query
        searchCallReference?.enqueue(apiCB)
    }

    fun loadMovies() {
        val call = movieDbClient.nowPlaying()
        activeQuery = null
        call.enqueue(apiCB)
    }

    /**
     * Loads the next set of movies for the current active state: now_playing/search
     * If a load is already in progress it does nothing
     */
    fun loadNextPage() {
        //side effect some times the scroll might get stop suddenly especially on spotty connections
        if(loadingData.get()) return

        loadingData.set(true)
        val call = if(activeQuery == null) {
            movieDbClient.nowPlaying(++currentPage)
        } else {
            movieDbClient.search(activeQuery ?: "",++currentPage)
        }

        call.enqueue(object: Callback<ApiMovieList>{
            override fun onFailure(call: Call<ApiMovieList>, t: Throwable) {
                Log.w("MovieListViewModel", t)
                loadingData.set(false)
            }

            override fun onResponse(call: Call<ApiMovieList>, response: Response<ApiMovieList>) {
                val body = response.body()
                if(response.isSuccessful && body != null) {
                    pagingMovieList.postValue(body.results)
                    loadingData.set(false)
                } else {
                    Log.w("MovieListViewModel", response.message())
                }
            }

        })
    }
}
