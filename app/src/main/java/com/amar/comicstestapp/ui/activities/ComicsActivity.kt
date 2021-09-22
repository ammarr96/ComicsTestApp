package com.amar.comicstestapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.amar.comicstestapp.R
import com.amar.comicstestapp.listeners.OnItemClickListener
import com.amar.comicstestapp.model.Comic
import com.amar.comicstestapp.ui.adapters.ComicListAdapter
import com.amar.comicstestapp.ui.viewmodel.ComicsViewModel
import com.amar.comicstestapp.util.Constants
import com.amar.comicstestapp.util.Helper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_comics.*

@AndroidEntryPoint
class ComicsActivity : AppCompatActivity() {

    private val comicList : ArrayList<Comic> = arrayListOf()
    val comicsViewModel: ComicsViewModel by viewModels()
    lateinit var comicAdapter: ComicListAdapter
    private var pageComics = 1
    private val limit = 15
    lateinit var listener: OnItemClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comics)

        listener = object : OnItemClickListener {
            override fun onComicSelected(item: Comic) {
                val intent = Intent(applicationContext, ComicDetailActivity::class.java)
                intent.putExtra(Constants.COMIC_ID, item.comicId)
                startActivity(intent)
            }
        }

        comicAdapter =  ComicListAdapter(comicList, listener)
        verticalRecyclerView.adapter = comicAdapter

        verticalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Helper.isInternetAvailable(applicationContext)) {
                        pageComics ++
                        loadMoreComics()
                    }
                }
            }
        })

        getData()
    }

    private fun getData() {

        showProgressBar(true)

        if (Helper.isInternetAvailable(applicationContext)) {
            comicsViewModel.getComicsFromAPIAndStore(limit, limit*pageComics)
        }

        else {
            comicsViewModel.getComicsFromLocalDatabase()
        }

        comicsViewModel.comicsLiveData.observe(this, Observer {
            comicList.clear()
            comicList.addAll(it)

            comicAdapter.setItems(comicList)
            comicAdapter.notifyDataSetChanged()
            showProgressBar(false)
        })
    }

    private fun loadMoreComics() {
        showProgressBar(true)
        comicsViewModel.getComicsFromAPIAndStore(limit, limit*pageComics)
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