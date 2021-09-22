package com.amar.comicstestapp.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.comicstestapp.model.Comic
import com.amar.comicstestapp.repository.ComicsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ComicDetailsViewModel @Inject constructor(
    private val comicsRepository: ComicsRepository
) : ViewModel() {

    var comicsLiveData: MutableLiveData<Comic> = MutableLiveData()

    init {
        comicsLiveData = comicsRepository.comic
    }

    fun getComicsDetailsFromAPIAndStore(comicId: Int,) {
        comicsRepository.getComicDetailsFromApi(comicId)
    }

    fun getComicDetailsFromLocalDatabase(comicId: Int) {
        comicsRepository.getComicDetailsFromLocalDataBase(comicId)
    }


}