package com.example.majikatubes1.data.menu

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.majikatubes1.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class MenuRepository {
    fun getMenu(): MutableLiveData<List<MenuModel>> {
        var result: MutableLiveData<List<MenuModel>> = MutableLiveData()

        var menuAPI: MenuAPI = RetrofitClient().getRetrofitClient()!!.create()
        var call: Call<MenuResponse> = menuAPI.getAllMenu()

        call.enqueue(object : Callback<MenuResponse> {
            override fun onResponse(
                call: Call<MenuResponse>,
                response: Response<MenuResponse>
            ) {
                result.value = response.body()?.data?.toList()
            }

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                Log.e("Failure Menu", "Failure $call")
                t.printStackTrace()
            }

        })

        return result
    }
}