package com.example.myquizgame.Backends


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GetQuestions {
    private const val BASE_URL = "https://opentdb.com"

    val instance: EndpointQuestions by lazy {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
        retrofit.create(EndpointQuestions::class.java)
    }
}
