package com.ionutlepi.movies.ui.movielist

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import com.ionutlepi.movies.Movie
import com.ionutlepi.movies.R

import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.v_movie_list_single_item.view.*


class MovieListAdapter(
    var list: MutableList<Movie> = mutableListOf(),
    private val navigationController: NavController,
    private var columnCount: Int = 2
) : RecyclerView.Adapter<MovieListAdapter.MovieListItem>() {

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MovieListItem {
        val inflater = LayoutInflater.from(parent.context).inflate(
            R.layout.movie_list_item,
            parent,
            false
        )
        return MovieListItem(inflater, navigationController)
    }

    override fun getItemCount(): Int  {
        val size =  (list.size / columnCount)
        return  if(list.size % columnCount == 0) {
            size
        } else {
            size + 1 // we need another row for remaining items
        }
    }


    override fun onBindViewHolder(viewHolder: MovieListItem, position: Int) {
        viewHolder.bindView(
            list[position * columnCount],
            list.getOrNull(position * columnCount + 1)
        )
    }


    fun setData(newData: List<Movie>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }

    fun addData(newData: List<Movie>) {
        val currentSize = itemCount
        list.addAll(newData)
        notifyItemRangeInserted(currentSize, newData.size)
    }

    class MovieListItem(view: View, private val navigationController: NavController) :
        RecyclerView.ViewHolder(view) {

        fun bindView(first: Movie, second: Movie? = null) {
            //TODO: this needs refactor to support multiple columns
            itemView.first.moviePoster.setImageURI(first.getMoviePosterUrl())
            itemView.first.setOnClickListener(MovieItemClickListener(first, navigationController))
            if(second == null) {
                itemView.second.visibility = View.INVISIBLE //we want to maintain the space occupied for constrain layout rules
            } else {
                itemView.second.moviePoster.setImageURI(second.getMoviePosterUrl())
                itemView.second.setOnClickListener(MovieItemClickListener(second, navigationController))
           }
        }
    }

    private class MovieItemClickListener(private val movie: Movie, private val navigationController: NavController): View.OnClickListener {
        override fun onClick(p0: View?) {
            val data = Bundle()
            data.putSerializable("movie",movie)
            navigationController.navigate(R.id.movieDetails, data)
        }

    }
}