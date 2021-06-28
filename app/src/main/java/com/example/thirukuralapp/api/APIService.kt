package com.example.thirukuralapp.api

import com.example.thirukuralapp.model.ThirukuralResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIService {
    @Headers("Accept: application/json")
    @GET("api")
    suspend fun getThirukural(@Query("num") number: Int): ThirukuralResponse

    companion object {
        var BASE_URL = "https://api-thirukkural.vercel.app/"
        fun create(): APIService {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(APIService::class.java)
        }
    }
}