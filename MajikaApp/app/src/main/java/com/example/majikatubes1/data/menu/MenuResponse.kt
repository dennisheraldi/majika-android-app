package com.example.majikatubes1.data.menu

import com.example.majikatubes1.data.cabang.CabangModel
import com.google.gson.annotations.SerializedName

data class MenuResponse(
    @SerializedName("data")
    val data: List<MenuModel>,
    @SerializedName("size")
    val size: Int
)