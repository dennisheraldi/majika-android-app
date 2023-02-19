package com.example.majikatubes1.data.pembayaran

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.majikatubes1.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.create
import retrofit2.Response

class PembayaranRepository {
    fun getStatusPembayaran(transactionId : String) : MutableLiveData<PembayaranModel> {
        var result: MutableLiveData<PembayaranModel>    = MutableLiveData()
        var pembayaranAPI: PembayaranAPI                = RetrofitClient().getRetrofitClient()!!.create()
        var call: Call<PembayaranResponse>              = pembayaranAPI.getStatusPembayaran(transactionId)

        call.enqueue(object : Callback<PembayaranResponse> {
            override fun onResponse(
                call: Call<PembayaranResponse>,
                response: Response<PembayaranResponse>
            ) {
                var responseData    = PembayaranStatus.valueOf(response.body()?.status.toString())
                result.value        = PembayaranModel(responseData)
            }

            override fun onFailure(call: Call<PembayaranResponse>, t: Throwable) {
                Log.e("Failure Menu", "Failure $call")
                t.printStackTrace()
            }
        })
        return result;
    }
}