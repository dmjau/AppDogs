package com.cursoandroid.appdogs


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET
    fun getDogByBreeds(@Url url: String): Call<DogsResponse>
}