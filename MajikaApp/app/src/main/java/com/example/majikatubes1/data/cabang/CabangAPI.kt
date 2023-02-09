package com.example.majikatubes1.data.cabang

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query;

interface CabangAPI {
    @GET("branch")
    fun getAllCabang(): Call<CabangResponse>
}