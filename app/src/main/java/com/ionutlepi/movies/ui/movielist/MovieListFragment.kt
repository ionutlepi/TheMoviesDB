package com.ionutlepi.movies.ui.movielist


import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ionutlepi.movies.HomeActivity
import com.ionutlepi.movies.R
import com.ionutlepi.movies.models.Movie
import kotlinx.android.synthetic.main.f_movie_list.*


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

    private fun onSearchTextChanged(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (!query.isBlank()) {
                    viewModel.search(query)
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                when {
                    !newText.isBlank() -> viewModel.search(newText)
                }
                return false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.f_movie_list, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.movie_list_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(onSearchTextChanged())
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val item = menu.findItem(R.id.search)
        val searchView = item.actionView as SearchView
        if (viewModel.activeQuery?.isNotBlank() == true) {
            item.expandActionView()
            searchView.clearFocus()
            searchView.setQuery(viewModel.activeQuery, false)
        }
        item.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean = true

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                viewModel.loadMovies()
                return true
            }

        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as HomeActivity).setActionBarTitle(R.string.app_name)
        movieList.apply {
            adapter = movieListAdapter
            addOnScrollListener(recyclerViewOnScrollListener)
        }
        viewModel.getMovies().observe(this, Observer { movieList: List<Movie>? ->
            if (movieList != null) {
                movieListAdapter.setData(movieList)
            }
        })
        viewModel.pagingMovieList.observe(this, Observer { movieList: List<Movie>? ->
            if (movieList != null) {
                movieListAdapter.addData(movieList)
            }
        })

    }

}
