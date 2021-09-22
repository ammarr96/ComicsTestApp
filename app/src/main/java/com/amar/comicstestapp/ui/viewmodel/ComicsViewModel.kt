package com.amar.comicstestapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.comicstestapp.model.Comic
import com.amar.comicstestapp.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComicsViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    var comicsLiveData: MutableLiveData<List<Comic>> = MutableLiveData()

    init {
        comicsLiveData = comicsRepository.comicList
    }

    fun getComicsFromAPIAndStore(limit: Int, offset: Int) {
        comicsRepository.getComicsFromApi(limit, offset)
    }

    fun getComicsFromLocalDatabase() {
       comicsRepository.getComicsFromLocalDataBase()
    }

}