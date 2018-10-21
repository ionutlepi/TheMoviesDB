package com.ionutlepi.movies.ui.movielist


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ionutlepi.movies.HomeActivity
import com.ionutlepi.movies.Movie
import com.ionutlepi.movies.R
import kotlinx.android.synthetic.main.f_movie_list.movieList
import android.support.v7.widget.SearchView
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView




class MovieListFragment : Fragment() {

    private val movieListAdapter: MovieListAdapter by lazy {
        MovieListAdapter(navigationController =  findNavController())
    } //otherwise we would not have a fragment manager available

    private val viewModel: MovieListViewModel by lazy {
        ViewModelProviders.of(this).get(MovieListViewModel::class.java)
    }

    //TODO: move to using paging library to load more items
    private val recyclerViewOnScrollListener = object : RecyclerView.OnScrollListener() {


        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
            ) {
                //this might get triggered multiple times in a row but it is guarded inside
                //until it is finished loading
                viewModel.loadNextPage()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.f_movie_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.movie_list_menu, menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if(!query.isBlank()) {
                    viewModel.search(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //TODO: defer call using rxjava
                if(!newText.isBlank()) {
                    viewModel.search(newText)
                } else {
                    viewModel.loadMovies()
                }
                return false
            }
        })
        searchView.setOnClickListener { }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).setActionBarTitle(R.string.app_name)
        movieList.apply {
            adapter = movieListAdapter
            addOnScrollListener(recyclerViewOnScrollListener)
        }
        viewModel.getMovies().observe(this, Observer{ movieList: List<Movie>? ->
            if(movieList != null) {
                movieListAdapter.setData(movieList)
            }
        })
        viewModel.pagingMovieList.observe(this, Observer{ movieList: List<Movie>? ->
            if(movieList != null) {
                movieListAdapter.addData(movieList)
            }
        })
    }

}
