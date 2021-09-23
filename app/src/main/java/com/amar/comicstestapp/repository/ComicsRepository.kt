package com.amar.comicstestapp.repository

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.amar.comicstestapp.database.ComicDAO
import com.amar.comicstestapp.model.Comic
import com.amar.comicstestapp.model.ComicsResponse
import com.amar.comicstestapp.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class ComicsRepository @Inject constructor(
    private val apiService: ApiService,
    private val comicDao: ComicDAO) {

    var comicList : MutableLiveData<List<Comic>>
    var comic : MutableLiveData<Comic>
    private var list: ArrayList<Comic> = ArrayList()

    init {
        comicList = MutableLiveData()
        comic = MutableLiveData()
    }

    fun getComicsFromApi(limit: Int, offset: Int)  {

        val call = apiService.getComics(limit, offset)

        call.enqueue(object : Callback<ComicsResponse?> {
            override fun onResponse(call: Call<ComicsResponse?>, response: Response<ComicsResponse?>) {

                when(response.code())
                {
                    200 ->{

                        list.addAll(response.body()?.data?.results ?: arrayListOf())
                        comicList.value = list

                        Thread(Runnable {

                            comicDao.deleteAll()
                            comicDao.insertComics(list)


                        }).start()

                    }
                }

            }

            override fun onFailure(call: Call<ComicsResponse?>, t: Throwable) {

            }

        })

    }

    fun getComicDetailsFromApi(comicId: Int)  {

        val call = apiService.getComicDetails(comicId)

        call.enqueue(object : Callback<ComicsResponse?> {
            override fun onResponse(call: Call<ComicsResponse?>, response: Response<ComicsResponse?>) {

                when(response.code())
                {
                    200 ->{

                        comic.value = response.body()?.data?.results?.get(0)

                        Thread(Runnable {

                            comicDao.insertSingleComic(comic.value!!)


                        }).start()

                    }
                }

            }

            override fun onFailure(call: Call<ComicsResponse?>, t: Throwable) {

            }

        })

    }

    fun getComicsFromLocalDataBase() {
        comicList.value = comicDao.getAllComics()
    }

    fun getComicDetailsFromLocalDataBase(comicId: Int) {
        comic.value = comicDao.getComicById(comicId)
    }



}