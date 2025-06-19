package com.mtg.randomcard.network

import com.mtg.randomcard.data.CardDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ScryfallApi {
    @GET("cards/random")
    suspend fun randomCard(@Query("q") q: String = "-t:land game:paper"): CardDto
}

object NetworkModule {
    private const val TIMEOUT = 20L // seconds

    private val okHttp by lazy {
        OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .build()
    }

    private val moshi by lazy {
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    }

    val api: ScryfallApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.scryfall.com/")
            .client(okHttp)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(ScryfallApi::class.java)
    }
}