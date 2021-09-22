package com.amar.comicstestapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amar.comicstestapp.model.Comic

@Dao
interface ComicDAO {

    @Query("SELECT * from comics")
    fun getAllComics(): List<Comic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComics(list: List<Comic>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSingleComic(list: Comic)

    @Query("SELECT * from comics WHERE comicId = :comicId")
    fun getComicById(comicId: Int): Comic

//    @Query("DELETE from comics WHERE comicId = :comicId")
//    fun deleteComiscById(comicId: Int)

    @Query("DELETE from comics")
    fun deleteAll()


}