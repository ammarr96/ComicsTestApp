package com.amar.comicstestapp.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.amar.comicstestapp.R
import com.amar.comicstestapp.model.Comic
import com.amar.comicstestapp.ui.viewmodel.ComicDetailsViewModel
import com.amar.comicstestapp.ui.viewmodel.ComicsViewModel
import com.amar.comicstestapp.util.Constants.COMIC_ID
import com.amar.comicstestapp.util.Helper
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_comic_detail.*

@AndroidEntryPoint
class ComicDetailActivity : AppCompatActivity() {

    private lateinit var comic : Comic
    val comicDetailsViewModel: ComicDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comic_detail)

        val comicId: Int =  intent.extras?.getInt(COMIC_ID, 0) ?: 0

        getData(comicId)

    }

    private fun getData(comicId: Int) {

        showProgressBar(true)

        if (Helper.isInternetAvailable(applicationContext)) {
            comicDetailsViewModel.getComicsDetailsFromAPIAndStore(comicId)
        }

        else {
            comicDetailsViewModel.getComicDetailsFromLocalDatabase(comicId)
        }

        comicDetailsViewModel.comicsLiveData.observe(this, Observer {
            if (it.comicId == comicId) {
                comic = it
                showProgressBar(false)
                showData()
            }
        })
    }

    private fun showData() {
        titleTV.text = comic.title
        if (!comic.description.isNullOrEmpty()) {
            descriptionTV.text = Html.fromHtml(comic.description, Html.FROM_HTML_MODE_COMPACT)
        }
        else {
            descriptionTV.text = getString(R.string.no_description_available)
        }
        Glide.with(applicationContext).load(comic.getImageUrl()).into(image)

    }

    private fun showProgressBar(show: Boolean) {
        if (show) {
            progressBar.visibility = View.VISIBLE
        }
        else {
            progressBar.visibility = View.GONE
        }
    }

}