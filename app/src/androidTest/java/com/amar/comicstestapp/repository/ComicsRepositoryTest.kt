package com.amar.comicstestapp.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.amar.comicstestapp.database.ComicDAO
import com.amar.comicstestapp.database.ComicsDatabase
import com.amar.comicstestapp.model.Comic
import com.amar.comicstestapp.model.Thumbnail
import com.amar.comicstestapp.network.ApiService
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class ComicsRepositoryTest {

    private lateinit var database: ComicsDatabase
    private lateinit var dao: ComicDAO

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), ComicsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.comicDao()
    }

    @Test
    fun getComicList() {

        val comicList: List<Comic> = mutableListOf<Comic>(
            Comic(1, "Title1", "Description1", Thumbnail("path1", "extension1")),
            Comic(2, "Title2", "Description2", Thumbnail("path2", "extension2")))

        dao.insertComics(comicList)

        val list =dao.getAllComics()

        assertEquals(2, list.size)
    }

    @Test
    fun getComic() {

        val comicList: List<Comic> = mutableListOf<Comic>(
            Comic(1, "Title1", "Description1", Thumbnail("path1", "extension1")),
            Comic(2, "Title2", "Description2", Thumbnail("path2", "extension2")))

        dao.insertComics(comicList)

        val item = dao.getComicById(1)

        assertEquals("Title1", item.title)
    }
}