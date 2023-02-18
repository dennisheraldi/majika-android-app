package com.example.majikatubes1.data.pembayaran

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path


interface PembayaranAPI {
    @POST("payment/{TransactionId}")
    fun getStatusPembayaran(
        @Path("TransactionId") transactionId : String) :
            Call<PembayaranResponse>
}