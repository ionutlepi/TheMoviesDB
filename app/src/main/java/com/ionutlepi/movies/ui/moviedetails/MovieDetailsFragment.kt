package com.ionutlepi.movies.ui.moviedetails

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.info.ImagePerfData
import com.facebook.drawee.backends.pipeline.info.ImagePerfDataListener
import com.facebook.drawee.backends.pipeline.info.ImagePerfUtils
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.ionutlepi.movies.HomeActivity
import com.ionutlepi.movies.R
import com.ionutlepi.movies.formmatForDisplay
import com.ionutlepi.movies.models.Movie
import kotlinx.android.synthetic.main.f_movie_details_ml.*
import timber.log.Timber

class MovieDetailsFragment : Fragment() {


    private val logImagePerf = object: ImagePerfDataListener {
        override fun onImageLoadStatusUpdated(imagePerfData: ImagePerfData?, imageLoadStatus: Int) {
            Timber.d("ImagePerf state=${ImagePerfUtils.toString(imageLoadStatus)}, data=${imagePerfData?.createDebugString()}")
        }

        override fun onImageVisibilityUpdated(imagePerfData: ImagePerfData?, visibilityState: Int) {
            // ignore
        }
    }

    private val movieDetailsLiveViewModel by lazy {
        ViewModelProviders.of(this).get(MovieDetailsLiveViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.f_movie_details_ml, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.get("movie") as Movie?
        movieDetailsLiveViewModel.movieLiveData.observe(this, Observer {
            Timber.d(it.toString())
        })
        movie?.apply {
            (activity as HomeActivity).setActionBarTitle(originalTitle)
            movieDetailsLiveViewModel.load(id)
            synopsis.text = this.description
            poster.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(getBackDropUrl()))
                        .build()
                )
                .setPerfDataListener(logImagePerf)
                .build()
            releasedDate.text = released.formmatForDisplay()
        }
    }

}
