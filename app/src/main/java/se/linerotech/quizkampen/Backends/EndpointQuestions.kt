package com.example.myquizgame.Backends

import com.example.myquizgame.models.Qustions
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EndpointQuestions {
    @GET("/api.php")
    fun getQuestions(
        @Query("amount") amount: String,
        @Query("token") token: String

    ): Call<Qustions>
}