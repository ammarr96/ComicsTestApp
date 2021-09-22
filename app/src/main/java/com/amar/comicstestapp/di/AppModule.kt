package com.amar.comicstestapp.di

import android.content.Context
import androidx.room.Room
import com.amar.comicstestapp.database.ComicDAO
import com.amar.comicstestapp.database.ComicsDatabase
import com.amar.comicstestapp.network.ApiService
import com.amar.comicstestapp.repository.ComicsRepository
import com.amar.comicstestapp.util.Constants
import com.amar.comicstestapp.util.Helper
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ComicsDatabase::class.java, "comics.db")
            .allowMainThreadQueries()
            .build()

    @Singleton
    @Provides
    fun provideComicDao(database: ComicsDatabase) =
        database.comicDao()

    @Singleton
    @Provides
    fun provideApiService() : ApiService {

        val httpClient = OkHttpClient.Builder().addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {

                val calendar: Calendar = Calendar.getInstance()

                val stringToHash: String = calendar.getTimeInMillis().toString() + Constants.PRIVATE_KEY + Constants.PUBLIC_KEY

                val original: Request = chain.request()
                val originalHttpUrl: HttpUrl = original.url
                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apikey", Constants.PUBLIC_KEY)
                    .addQueryParameter("hash", Helper.md5Java(stringToHash))
                    .addQueryParameter("ts", calendar.getTimeInMillis().toString())
                    .build()

                val requestBuilder: Request.Builder = original.newBuilder()
                    .url(url)
                val request: Request = requestBuilder.build()
                return chain.proceed(request)

            }

        })
        httpClient.readTimeout(240, TimeUnit.SECONDS)
        httpClient.connectTimeout(240, TimeUnit.SECONDS)
        httpClient.writeTimeout(240, TimeUnit.SECONDS)


        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .build()
            .create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService, comicDao: ComicDAO
    ) = ComicsRepository(apiService, comicDao)

}