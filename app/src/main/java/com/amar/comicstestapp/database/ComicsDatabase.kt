package com.amar.comicstestapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.amar.comicstestapp.model.Comic


@Database(entities = [Comic::class], version = 3)
abstract class ComicsDatabase: RoomDatabase() {

    abstract fun comicDao(): ComicDAO

}