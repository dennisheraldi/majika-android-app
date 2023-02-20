package com.example.majikatubes1.data

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    private var retrofit: Retrofit? = null

    fun getRetrofitClient(): Retrofit? {
        if (retrofit == null) {
            val client = OkHttpClient.Builder().build()
            retrofit = Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.18.7:3000/v1/")
                .build()
        }
        return retrofit
    }
}