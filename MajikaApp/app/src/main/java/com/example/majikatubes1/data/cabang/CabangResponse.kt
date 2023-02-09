package com.example.majikatubes1.data.cabang

import com.google.gson.annotations.SerializedName

data class CabangResponse(
    @SerializedName("data")
    val data: List<CabangModel>,
    @SerializedName("size")
    val size: Int
)