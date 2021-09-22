package com.amar.comicstestapp.network

import com.amar.comicstestapp.model.ComicsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @GET("comics")
    fun getComics(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<ComicsResponse>

    @GET("comics/{comic_id}")
    fun getComicDetails(@Path("comic_id") charcterId: Int,): Call<ComicsResponse>

}