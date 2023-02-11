package com.example.majikatubes1.data.menu

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query;


interface MenuAPI {
    @GET("menu")
    fun getAllMenu(): Call<MenuResponse>
}