package com.ionutlepi.movies.ui.moviedetails

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.info.ImagePerfData
import com.facebook.drawee.backends.pipeline.info.ImagePerfDataListener
import com.facebook.drawee.backends.pipeline.info.ImagePerfUtils
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.ionutlepi.movies.HomeActivity

import com.ionutlepi.movies.R
import com.ionutlepi.movies.formmatForDisplay
import com.ionutlepi.movies.models.Movie
import kotlinx.android.synthetic.main.f_movie_details_ml.poster
import kotlinx.android.synthetic.main.f_movie_details_ml.releasedDate
import kotlinx.android.synthetic.main.f_movie_details_ml.synopsis
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.f_movie_details_ml, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val movie = arguments?.get("movie") as Movie?

        movie?.apply {
            (activity as HomeActivity).setActionBarTitle(originalTitle)
            synopsis.text = this.description
            poster.controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(
                    ImageRequestBuilder.newBuilderWithSource(Uri.parse(getBackDropUrl()))
                        .build()
                )
                .setAutoPlayAnimations(true)
                .setPerfDataListener(logImagePerf)
                .build()
            releasedDate.text = released.formmatForDisplay()
        }
    }

}
