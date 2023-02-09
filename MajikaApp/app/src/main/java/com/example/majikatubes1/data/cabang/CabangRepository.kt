package com.example.majikatubes1.data.cabang

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.majikatubes1.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class CabangRepository {
    fun getCabang(): MutableLiveData<List<CabangModel>> {
        var result: MutableLiveData<List<CabangModel>> = MutableLiveData()

        var cabangAPI: CabangAPI = RetrofitClient().getRetrofitClient()!!.create()
        var call: Call<CabangResponse> =  cabangAPI.getAllCabang()

        call.enqueue(object : Callback<CabangResponse> {
            override fun onResponse(
                call: Call<CabangResponse>,
                response: Response<CabangResponse>
            ) {
                result.value = response.body()?.data?.toList()
            }

            override fun onFailure(call: Call<CabangResponse>, t: Throwable) {
                Log.e("Failure Cabang", "Failure $call")
                t.printStackTrace()
            }


        })

        return result
    }
}
