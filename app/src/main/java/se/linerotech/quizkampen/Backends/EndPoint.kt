package com.example.myquizgame

import com.example.myquizgame.models.Token
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EndpointToken {
    @GET("/api_token.php")
    fun getToken(@Query("command") command: String)
    : Call<Token>
}